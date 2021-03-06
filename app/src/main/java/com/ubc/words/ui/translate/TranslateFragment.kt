package com.ubc.words.ui.translate

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.ubc.words.App
import com.ubc.words.R
import com.ubc.words.databinding.FragmentTranslateBinding
import com.ubc.words.util.Language
import org.koin.androidx.viewmodel.ext.android.viewModel

class TranslateFragment : Fragment(){

    private lateinit var binding: FragmentTranslateBinding
    private val apiKey =
        "dict.1.1.20210824T194936Z.5698033a1adbc74d.a86d9902160b14f05e95876b2735786b9cb5224d"
    private val lang1 = "en-ru"
    private val lang2 = "ru-en"
    private val translateViewModel by viewModel<TranslateViewModel>()
    private val app : App = App()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentTranslateBinding.inflate(inflater, container, false)

        val translateWordsObserver = Observer<List<String>?> {
            val translatedWordsAdapter = ArrayAdapter<String>(
                requireContext(),
                android.R.layout.simple_list_item_1,
                it
            )
            if (it.size > 0) {
                binding.editSecondLanguage.setText(it.get(0))
                binding.listOfTranslatedWords.adapter = translatedWordsAdapter
            } else {
                binding.editSecondLanguage.text = binding.editFirstLanguage.text
            }
        }

        translateViewModel.translatedWords.observe(viewLifecycleOwner, translateWordsObserver)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var translatedWordsAdapter: ArrayAdapter<String> = ArrayAdapter<String>(
            requireContext(),
            android.R.layout.simple_list_item_1,
        )
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

        binding.listOfTranslatedWords.setOnItemClickListener { parent, view, position, id ->
            binding.editSecondLanguage.setText(
                binding.listOfTranslatedWords.getItemAtPosition(position).toString())
        }
    }

    private fun goToLink(link: String) {
        val page: Intent = Intent(Intent.ACTION_VIEW)
        page.setData(Uri.parse(link))
        startActivity(page)
    }

    private fun switchLanguage() {
        val firstLanguage = binding.textFirstLanguage.text.toString()
        when (firstLanguage) {
            Language.English.toString() -> switchToRussian()
            Language.Russian.toString() -> switchToEnglish()
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
        val text = binding.editFirstLanguage.text.toString()
        if (text.equals("")) {
            Toast.makeText(
                requireContext(),
                "???? ???? ?????????? ??????????.",
                Toast.LENGTH_SHORT
            ).show()
        } else if (isAlphabet(text)) {
            var lang = ""
            if (binding.textFirstLanguage.text == "English") {
                lang = lang1
            } else {
                lang = lang2
            }
            val url: String =
                "https://dictionary.yandex.net/api/v1/dicservice.json/lookup?key=" + apiKey + "&text=" + text + "&lang=" + lang
            translateViewModel.queryToDictionaryApi(url)
        }
    }

    private fun addToDictionary() {
        if (binding.editFirstLanguage.text != null && binding.editFirstLanguage.text.toString() != ""  &&
            binding.editSecondLanguage.text != null && binding.editSecondLanguage.text.toString() != "") {

            val engWord: String; val rusWord: String
            if (binding.textFirstLanguage.text == "English") {
                engWord = binding.editFirstLanguage.text.toString()
                rusWord = binding.editSecondLanguage.text.toString()
            } else {
                engWord = binding.editFirstLanguage.text.toString()
                rusWord = binding.editFirstLanguage.text.toString()
            }

            if (translateViewModel.isPairOfWordsInDict(engWord, rusWord)) {
                Toast.makeText(
                    requireContext(),
                    "?????? ???????? ???????? ?????? ???????? ?? ??????????????",
                    Toast.LENGTH_LONG
                ).show()
            } else {
                translateViewModel.setDictionary(engWord, rusWord)
            }
        } else {
            Toast.makeText(
                requireContext(),
                "?????????????????? ????????????",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}