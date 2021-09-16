package com.example.yandextestapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import com.example.yandextestapp.databinding.ActivityMainBinding
import com.example.yandextestapp.entities.Currency
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
        binding.mainFromAmountEdit.text = binding.mainToAmountEdit.text.also {
            binding.mainToAmountEdit.text = binding.mainFromAmountEdit.text
        }

        val selectedToItem = binding.mainSpinnerTo.selectedItemPosition
        binding.mainSpinnerTo.setSelection(binding.mainSpinnerFrom.selectedItemPosition)
        binding.mainSpinnerFrom.setSelection(selectedToItem)

        view.animate()
            .rotationBy(180f).apply {
                duration = 350
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
            binding.mainFromAmountEdit.error = getString(R.string.error_empty)
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

    private fun setState(loading: Boolean) {
        if (loading) {
            binding.apply {
                mainConvertButton.isEnabled = false
                mainProgress.isVisible = true
                mainSwitchButton.isInvisible = true
                mainSwitchButton.isEnabled = false
            }
        } else {
            binding.apply {
                mainConvertButton.isEnabled = true
                mainProgress.isInvisible = true
                mainSwitchButton.isVisible = true
                mainSwitchButton.isEnabled = true
            }
        }
    }

    private fun initialLoad() {
        setState(true)
        convertRepo.getCurrencies()
            .map { currencies -> currencies.map(Currency::code) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ currenciesCodes ->
                setState(false)
                adapterFrom.addAll(currenciesCodes)
                adapterTo.addAll(currenciesCodes)
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
