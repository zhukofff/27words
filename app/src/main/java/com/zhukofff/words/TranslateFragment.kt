package com.zhukofff.words

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.zhukofff.words.databinding.FragmentTranslateBinding

class TranslateFragment : Fragment() {

    private lateinit var binding : FragmentTranslateBinding
    lateinit var sharedPreference: SharedPreferences
    private val apiKey = "dict.1.1.20191113T195139Z.4c9beedf28f906fa.c7c3b41be62bf79ba48a96bf1079d96e5db6562d"
    private val lang1 = "en-ru"
    private val lang2 = "ru-en"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) : View {
        binding = FragmentTranslateBinding.inflate(inflater, container, false)
        sharedPreference = this.requireActivity().getSharedPreferences("com.zhukofff.words", Context.MODE_PRIVATE)

        binding.creatingByUbc.setOnClickListener {
            goToLink("ds.ubc.one")
        }

        binding.textTranslatedByYandex.setOnClickListener {
            goToLink("http://translate.yandex.ru")
        }

        binding.changeLanguage.setOnClickListener {
            switchLanguage()
        }

        binding.translate.setOnClickListener {
            // maybe realise hide keyboard method
            val text : String = binding.editFirstLanguage.text.toString()
            if (text == null || text.equals("")) {
                Toast.makeText(requireContext(),
                    "Вы не ввели слово.",
                    Toast.LENGTH_SHORT).
                        show()
            } else if (isAlphabet(text)) {
                val url : String = "https://dictionary.yandex.net/api/v1/dicservice.json/lookup?key=" + apiKey + "&text=" + text + "&lang=" + lang2
                // how to download html?
            }
        }

        return binding.root
    }

    private fun goToLink(link : String) {
        val page: Intent = Intent(Intent.ACTION_VIEW)
        page.setData(Uri.parse(link))
        startActivity(page)
    }

    private fun switchLanguage() {
        var firstLanguage = binding.textFirstLanguage.toString()
        when (firstLanguage) {
            Language.English.toString() -> switchToRussian()
            Language.Russian.toString() -> switchToEnglish()
        }

    }

    private fun switchToRussian() {
        binding.textFirstLanguage.text = "${getString(R.string.russian)}"
        binding.textSecondLanguage.text = "${getString(R.string.english)}"
        binding.editFirstLanguage.hint =  "${getString(R.string.russianEditTextHint)}"
        binding.editSecondLanguage.hint = "${getString(R.string.englishEditTextHint)}"
    }

    private fun switchToEnglish() {
        binding.textFirstLanguage.text = "${getString(R.string.english)}"
        binding.textSecondLanguage.text = "${getString(R.string.russian)}"
        binding.editFirstLanguage.hint =  "${getString(R.string.englishEditTextHint)}"
        binding.editSecondLanguage.hint = "${getString(R.string.russianEditTextHint)}"
    }

    private fun isAlphabet(text : String) : Boolean{
        return text.all {
            it.isLetter()
        }
    }
}