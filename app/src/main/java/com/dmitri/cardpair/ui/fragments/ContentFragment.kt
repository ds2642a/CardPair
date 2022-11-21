package com.dmitri.cardpair.ui.fragments

import android.app.AlertDialog
import android.content.res.AssetManager
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.dmitri.cardpair.AFConversionData
import com.dmitri.cardpair.R
import com.dmitri.cardpair.databinding.FragmentContentBinding
import com.dmitri.cardpair.model.Card
import com.dmitri.cardpair.ui.adapters.CardAdapter
import com.dmitri.cardpair.ui.adapters.CardItemDecoration
import java.io.InputStream
import kotlin.random.Random
import java.util.*


class ContentFragment : Fragment(R.layout.fragment_content) {
    lateinit var binding : FragmentContentBinding

    private var cards : ArrayList<Card> = ArrayList<Card>()
    private var shownCard : Card? = null
    private var reactToCardClick : Boolean = true

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentContentBinding.bind(view)

        initConversionDataButton()
        initCards()
    }

    private fun initConversionDataButton() {
        binding.conversionDataButton.setOnClickListener {
            val conversionData = AFConversionData.instance.conversionData

            if (conversionData != null) {
                var dialogMessage : String = ""
                for (data in conversionData) {
                    dialogMessage += "${data.key} : ${data.value}\n"
                }
                showDialog("Conversion data", dialogMessage)
            } else {
                Toast.makeText(context, "no conversion data yet...", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showDialog(title : String, message : String) {
        val builder = AlertDialog.Builder(context, R.style.AlertDialog)
        builder.setTitle(title)
        builder.setMessage(message)

        builder.setPositiveButton(android.R.string.yes) { dialog, which ->
            dialog.dismiss()
        }

        builder.show()
    }

    private fun initCards() {
        val assetManager: AssetManager? = context?.assets
        val imageNames : Array<String> = assetManager?.list("card_images") as Array<String>

        imageNames.shuffle(Random(java.util.Random().nextInt()))

        for ((pos, imageName) in imageNames.withIndex()) {
            Log.d("ContentFragmentTag", imageName)

            val ims : InputStream = assetManager.open("card_images/$imageName")
            val d : Drawable = Drawable.createFromStream(ims, null)
            ims.close()

            cards.add(Card(pos, d, R.drawable.back, imageName))
        }

        binding.cardRecyclerView.layoutManager = GridLayoutManager(context, 3)
        binding.cardRecyclerView.adapter = CardAdapter(cards) {position : Int -> onCardClicked(position)}
        val spacingInPixels = 10
        binding.cardRecyclerView.addItemDecoration(CardItemDecoration(spacingInPixels))
    }

    private fun onCardClicked(position : Int) {
        if (!reactToCardClick) return

        val clickedCard : Card = cards[position]

        if (shownCard == null) {
            clickedCard.isShown = true
            shownCard = clickedCard

            binding.cardRecyclerView.adapter?.notifyItemChanged(shownCard!!.pos)
        } else {
            if (shownCard!!.name == clickedCard.name) return

            clickedCard.isShown = true

            binding.cardRecyclerView.adapter?.notifyItemChanged(clickedCard.pos)

            reactToCardClick = false

            Handler(Looper.getMainLooper()).postDelayed({
                if (isPairCards(clickedCard, shownCard!!)) {
                    shownCard!!.isSolved = true
                    clickedCard.isSolved = true
                }

                shownCard!!.isShown = false
                binding.cardRecyclerView.adapter?.notifyItemChanged(shownCard!!.pos)
                shownCard = null
                clickedCard.isShown = false
                binding.cardRecyclerView.adapter?.notifyItemChanged(clickedCard.pos)
                reactToCardClick = true
                checkIfWin()
            }, 1000)
        }
    }

    private fun isPairCards(cardOne : Card, cardTwo : Card) : Boolean {
        val cardOneType : String = cardOne.name.split("_").toTypedArray()[1]
        val cardTwoType : String = cardTwo.name.split("_").toTypedArray()[1]
        return cardOneType == cardTwoType
    }

    private fun checkIfWin() {
        for (card in cards) {
            if (!card.isSolved) return
        }
        Toast.makeText(context, "YOU WIN!", Toast.LENGTH_LONG).show()
        cards.clear()
        initCards()
    }
}