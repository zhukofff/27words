package com.zhukofff.words

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.zhukofff.words.databinding.FragmentTranslateBinding
import org.json.JSONObject

class TranslateFragment : Fragment() {

    private lateinit var binding: FragmentTranslateBinding
    lateinit var sharedPreference: SharedPreferences
    private val apiKey =
        "dict.1.1.20191113T195139Z.4c9beedf28f906fa.c7c3b41be62bf79ba48a96bf1079d96e5db6562d"
    private val lang1 = "en-ru"
    private val lang2 = "ru-en"
    private val translateViewModel = TranslateViewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTranslateBinding.inflate(inflater, container, false)
        sharedPreference =
            this.requireActivity().getSharedPreferences("com.zhukofff.words", Context.MODE_PRIVATE)

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
            translate()
        }

        binding.addToDict.setOnClickListener {
                addToDictionary()
        }

        return binding.root
    }

    private fun goToLink(link: String) {
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
        binding.translate.setOnClickListener {

        }
    }

    private fun switchToRussian() {
        binding.textFirstLanguage.text = "${getString(R.string.russian)}"
        binding.textSecondLanguage.text = "${getString(R.string.english)}"
        binding.editFirstLanguage.hint = "${getString(R.string.russianEditTextHint)}"
        binding.editSecondLanguage.hint = "${getString(R.string.englishEditTextHint)}"
    }

    private fun switchToEnglish() {
        binding.textFirstLanguage.text = "${getString(R.string.english)}"
        binding.textSecondLanguage.text = "${getString(R.string.russian)}"
        binding.editFirstLanguage.hint = "${getString(R.string.englishEditTextHint)}"
        binding.editSecondLanguage.hint = "${getString(R.string.russianEditTextHint)}"
    }

    private fun isAlphabet(text: String): Boolean {
        return text.all {
            it.isLetter()
        }
    }

    private fun translate() {
        val text: String = binding.editFirstLanguage.text.toString()
        if (text == null || text.equals("")) {
            Toast.makeText(
                requireContext(),
                "Вы не ввели слово.",
                Toast.LENGTH_SHORT
            ).show()
        } else if (isAlphabet(text)) {
            val url: String =
                "https://dictionary.yandex.net/api/v1/dicservice.json/lookup?key=" + apiKey + "&text=" + text + "&lang=" + lang2
            try {
                binding.editSecondLanguage.text = "" as Editable
                val translatedWords = ArrayList<String>()
                val translatedWordsAdapter = ArrayAdapter<String>(
                    requireContext(),
                    android.R.layout.simple_list_item_1,
                    translatedWords
                )
                binding.listOfTranslatedWords.adapter = translatedWordsAdapter
                translateViewModel.downloadHtml(url)
                val jsonObject = JSONObject(translateViewModel.translatedWords)
                val defArray = jsonObject.getJSONArray("def")
                if (defArray.length() == 0)
                    binding.editSecondLanguage.text = binding.editFirstLanguage.text
                else {
                    for (i in 0..defArray.length()) {
                        val defObject = defArray.getJSONObject(i)
                        val trArray = defObject.getJSONArray("tr")
                        for (j in 0..trArray.length()) {
                            val trArrayItem = trArray.getJSONObject(j)
                            translatedWords.add(trArrayItem.getString("text"))
                        }
                    }
                    translatedWordsAdapter.notifyDataSetChanged()
                    binding.editSecondLanguage.text = translatedWords.get(0) as Editable
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun addToDictionary() {
        if (binding.editFirstLanguage.text != null && binding.editFirstLanguage.text != "" as Editable &&
            binding.editSecondLanguage.text != null && binding.editSecondLanguage.text != "" as Editable
        ) {

            val engWord: String; val rusWord: String
            if (binding.textFirstLanguage.text == getString(R.string.english) as Editable) {
                engWord = binding.textFirstLanguage.text.toString()
                rusWord = binding.textSecondLanguage.text.toString()
            } else {
                engWord = binding.textSecondLanguage.text.toString()
                rusWord = binding.textSecondLanguage.text.toString()
            }

            if (translateViewModel.isPairOfWordsInDict(engWord, rusWord)) {
                Toast.makeText(
                    requireContext(),
                    "Эта пара слов уже есть в словаре",
                    Toast.LENGTH_LONG
                ).show()
            } else {
                translateViewModel.addToDictionary(engWord, rusWord)
                try {
                    sharedPreference.edit().putString("dict", translateViewModel.studyFragment.dict.get(translateViewModel.studyFragment.dict.size)).apply()
                    sharedPreference.edit().putString("dict", translateViewModel.studyFragment.dict.get(translateViewModel.studyFragment.dict.size - 1)).apply()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        } else {
            Toast.makeText(
                requireContext(),
                "Добавлять нечего",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

}