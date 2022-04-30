package com.fifteenpuzzle

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleOwner
import com.fifteenpuzzle.databinding.MainActivityBinding
import com.fifteenpuzzle.ui.main.Position
import java.util.Collections.shuffle
import java.util.stream.IntStream
import kotlin.streams.toList

class MainActivity : AppCompatActivity(), LifecycleOwner {
    private lateinit var binding: MainActivityBinding
    // How many tiles wide or tall
    private var matrixSize = 4
    // dp width/height of each button
    private var buttonSize = 250
    // left margin dp
    private var tableOffsetX = 50
    // top margin dp
    private var tableOffsetY = 400
    // a list of buttons that are displayed on my board
    private var buttons = mutableListOf<Button>()


    // the entry point of program, launches
    // when the activity first loaded.
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainActivityBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        //initialize the game
        initGame()
    }

    // Initially sets the tiles on the board
    private fun initGame() {
        // Create a list of numbers 0 - matrix size
        val l1 = IntStream.range(0, matrixSize).toList().toMutableList()
        // Create another list of numbers 0 - matrix size
        val l2 = IntStream.range(0, matrixSize).toList().toMutableList()
        // shuffle both lists
        //shuffle(l1) // example { 0, 3, 1, 2}
        //shuffle(l2) // example { 1, 3, 2, 0}

        //iterate each value in l1 as x
        l1.forEach { x ->
            //iterate each value in l2 as y
            l2.forEach { y ->
                // pass x and y to create button
                val button = createButton(y.toFloat(), x.toFloat())
                // adds it to the list
                buttons.add(button)
                // adds it to the layout at calculated x, y coordinate
                binding.container.addView(button)
            }
        }
    }

    // creates and returns a button based on the x and y
    private fun createButton(x: Float, y: Float): Button {
        // create default empty button
        val button = Button(this)

        //calculate x and y coordinate for buttons
        button.x = x * buttonSize + tableOffsetX
        button.y = y * buttonSize + tableOffsetY
        // set width and height
        button.width = buttonSize
        button.height = buttonSize
        // generate a unique id for button
        button.id = View.generateViewId()
        // Sets click listener
        button.setOnClickListener {
            moveButton(button)
        }
        // display the id number as text
        button.text = "${button.id}"
        // set background color
        button.background.setTint(Color.argb(100,3,218,197))
        // if it's the last button, configure it to be an
        // empty space on the board
        if (button.id == matrixSize * matrixSize) {
            button.text = " "
            button.tag = "target"
            button.background.setTint(Color.WHITE)
        }
        return button
    }

    // moves the button when
    private fun moveButton(currentButton: Button) {
        // Grabs adjacent positions that are valid
        //and stores them into a list
        val neighbors =
            Position(
                (currentButton.x - tableOffsetX) / buttonSize,
                (currentButton.y - tableOffsetY) / buttonSize
            ).neighbors(
                matrixSize
            )
        // retrieve the empty button
        val button = binding.container.findViewWithTag<Button>("target")
        // calculate integer x value
        val x = (button.x - tableOffsetX) / buttonSize
        // calculate integer y value
        val y = (button.y - tableOffsetY) / buttonSize
        //is the empty part of the board contained within the neighbors
        //of the button that just got clicked
        if (neighbors.contains(Position(x, y))) {
            // Calculate how far the tapped should move to the next position
            val nextPC = Position(currentButton.x, currentButton.y) - Position(button.x, button.y)
            // Calculate how far the empty button should be moved to the next position
            val nextPN = Position(button.x, button.y) - Position(currentButton.x, currentButton.y)
            // change the x coordinate to the new position
            currentButton.translationX = currentButton.x + nextPC.x
            //change the y coordinate to the new position
            currentButton.translationY = currentButton.y + nextPC.y
            // move the empty button to the new position
            button.translationX = button.x + nextPN.x
            // move the empty button to the new position
            button.translationY = button.y + nextPN.y

        } else {
            Log.i("Move", "invalid")
        }
        // Check to see if the game is won
        if (check()) {
            Toast.makeText(this, "Congratulations", Toast.LENGTH_SHORT).show()
        }
    }

    private fun check(): Boolean {
        // create a boolean flag and setting to true
        var b = true
        // create a list of buttons
        val bttns = mutableListOf<Button>()
        // for 1 to matrix * matrix, grab each button
        // off the layout, and add it to the list
        IntStream.range(1, matrixSize * matrixSize + 1).forEach {
            //create a button, retreiving from the layout
            val button = binding.container.findViewById<Button>(it)
            bttns += button
        }
        // starting index at 0
        var index = 0
        // for 0 up to matrixSize
        IntStream.range(0, matrixSize).forEach { i ->
            IntStream.range(0, matrixSize).forEach { j ->
                // if the button at index is not in the calculated position
                // where it should be on the grid, set the boolean flag
                // to false
                if (bttns[index].x  != j * buttonSize.toFloat() + tableOffsetX
                    || bttns[index].y  != i * buttonSize.toFloat() + tableOffsetY) {
                    b = false
                }
                // advance to the next button in the list
                index++
            }
        }
        //return the boolean flag
        return b
    }


}