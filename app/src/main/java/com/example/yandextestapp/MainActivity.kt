package com.example.yandextestapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.LinearInterpolator
import android.view.animation.RotateAnimation
import android.widget.ArrayAdapter
import android.widget.Toast
import com.example.yandextestapp.databinding.ActivityMainBinding
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

class MainActivity : AppCompatActivity() {

    private lateinit var convertRepo: ConvertRepoImpl

    private lateinit var binding: ActivityMainBinding
    private val adapterFrom: ArrayAdapter<String> by lazy {
        ArrayAdapter(this, android.R.layout.simple_spinner_item)
    }
    private val adapterTo: ArrayAdapter<String> by lazy {
        ArrayAdapter(this, android.R.layout.simple_spinner_item)
    }
    private val disposables = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        convertRepo = ConvertRepoImpl(this)
        binding.mainSpinnerFrom.adapter = adapterFrom
        binding.mainSpinnerTo.adapter = adapterTo

        binding.mainConvertButton.setOnClickListener(::onConvertButtonClick)
        binding.mainSwitchButton.setOnClickListener(::onSwitchButtonClick)
        initialLoad()
    }

    private fun onSwitchButtonClick(view: View) {
        val temp = binding.mainFromAmountEdit.text.toString()
        binding.mainFromAmountEdit.text = binding.mainToAmountEdit.text
        binding.mainToAmountEdit.setText(temp)
        view.animate()
            .rotationBy(180f).apply {
                duration = 500
                interpolator = AccelerateInterpolator()
            }.withStartAction {
                view.isClickable = false
            }
            .withEndAction {
                view.isClickable = true
            }
    }

    private fun onConvertButtonClick(view: View) {
        if (binding.mainFromAmountEdit.text.isNullOrBlank()) {

        } else {
            convertRepo.convert(
                binding.mainFromAmountEdit.text.toString().toFloat(),
                binding.mainSpinnerFrom.selectedItem as String,
                binding.mainSpinnerTo.selectedItem as String,
            )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { am ->
                        binding.mainToAmountEdit.setText(am.toString())
                    },
                    {
                        Toast.makeText(
                            this,
                            "Sorry, error ${it.localizedMessage}",
                            Toast.LENGTH_LONG
                        )
                            .show()
                    }
                )
                .also {
                    disposables.add(it)
                }
        }
    }

    private fun initialLoad() {
        convertRepo.getCurrencies()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ items ->
                adapterFrom.addAll(items)
                adapterTo.addAll(items)
            },
                {
                    Toast.makeText(this, "Sorry, error ${it.localizedMessage}", Toast.LENGTH_LONG)
                        .show()
                }
            )
            .also {
                disposables.add(it)
            }
    }
}
