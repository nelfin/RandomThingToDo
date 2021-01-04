package `in`.nelf.randomthingtodo

import androidx.appcompat.app.AppCompatActivity
import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
class FullscreenActivity : AppCompatActivity() {
    private lateinit var fullscreenContent: TextView
    private lateinit var fullscreenContentControls: LinearLayout
    private val hideHandler = Handler()

    @SuppressLint("InlinedApi")
    private val hidePart2Runnable = Runnable {
        // Delayed removal of status and navigation bar

        // Note that some of these constants are new as of API 16 (Jelly Bean)
        // and API 19 (KitKat). It is safe to use them, as they are inlined
        // at compile-time and do nothing on earlier devices.
        fullscreenContent.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LOW_PROFILE or
                        View.SYSTEM_UI_FLAG_FULLSCREEN or
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
                        View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
    }
    private val showPart2Runnable = Runnable {
        // Delayed display of UI elements
        supportActionBar?.show()
        fullscreenContentControls.visibility = View.VISIBLE
    }
    private var isFullscreen: Boolean = false

    private val hideRunnable = Runnable { hide() }

    /**
     * Touch listener to use for in-layout UI controls to delay hiding the
     * system UI. This is to prevent the jarring behavior of controls going away
     * while interacting with activity UI.
     */
    private val delayHideTouchListener = View.OnTouchListener { view, motionEvent ->
        when (motionEvent.action) {
            MotionEvent.ACTION_DOWN -> if (AUTO_HIDE) {
                delayedHide(AUTO_HIDE_DELAY_MILLIS)
            }
            MotionEvent.ACTION_UP -> view.performClick()
            else -> {
            }
        }
        false
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_fullscreen)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        // Set up the user interaction to manually show or hide the system UI.
        fullscreenContent = findViewById(R.id.fullscreen_content)
        fullscreenContent.setOnClickListener { toggle() }
        toggle()
        fullscreenContentControls = findViewById(R.id.fullscreen_content_controls)

        // Upon interacting with UI controls, delay any scheduled hide()
        // operations to prevent the jarring behavior of controls going away
        // while interacting with the UI.
        findViewById<Button>(R.id.dummy_button).setOnTouchListener(delayHideTouchListener)
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        delayedHide(100)
    }

    private fun toggle() {
        val thingToDo = getThing()
        fullscreenContent.setText(thingToDo)
    }

    private fun hide() {
        // Hide UI first
        supportActionBar?.hide()
        fullscreenContentControls.visibility = View.GONE
        isFullscreen = false

        // Schedule a runnable to remove the status and navigation bar after a delay
        hideHandler.removeCallbacks(showPart2Runnable)
        hideHandler.postDelayed(hidePart2Runnable, UI_ANIMATION_DELAY.toLong())
    }

    private fun show() {
        // Show the system bar
        fullscreenContent.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                        View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
        isFullscreen = true

        // Schedule a runnable to display UI elements after a delay
        hideHandler.removeCallbacks(hidePart2Runnable)
        hideHandler.postDelayed(showPart2Runnable, UI_ANIMATION_DELAY.toLong())
    }

    /**
     * Schedules a call to hide() in [delayMillis], canceling any
     * previously scheduled calls.
     */
    private fun delayedHide(delayMillis: Int) {
        hideHandler.removeCallbacks(hideRunnable)
        hideHandler.postDelayed(hideRunnable, delayMillis.toLong())
    }

    private fun getThing(): String {
        val things = listOf(
                "Watch TV",
                "Eat a snack",
                "Argue with ya mum",
                "Check how much money is in your wallet",
                "Find your important documents",
                "Go to the sauna",
                "Get a coffee",
                "Check the weather for tomorrow",
                "Check how much ice is in the freezer",
                "Wash the dishes",
                "Re-arrange the furniture",
                "Make sure your backpack has a pen in it",
                "Fold your laundry",
                "Check the house for mould",
                "Check your tire pressure",
                "Try and make sherbert",
                "Wipe down the benches",
                "Draw a camel",
                "See how far you can spit",
                "Adjust the backlight on your TV",
                "Tell your friends how bad their taste in music is",
                "Check your MyGov inbox",
                "Close all of your tabs",
                "Put chilli in your nose",
                "Make sure all the fruit isn't rotten",
                "Ignore this suggestion",
                "Do 5 star jumps",
                "Stare at the wall for exactly 4 minutes",
                "Try and list the last five meals you ate",
                "Drink a tablespoon of straight olive oil",
                "Get rid of any spiders in the backyard",
                "Set up a rain gauge",
                "Re-oil the deck",
                "Charge up your phone battery bank",
                "Check to see if you have spare batteries",
                "Change the battery in your smoke alarms",
                "Eat a piece of toast",
                "Drink a mug of black coffee in less than 60 seconds",
                "Unpair your socks",
                "Drink some water",
                "Pet your dog",
                "Write down the excuses you say",
                "Make your bed",
                "Dust bookshelves",
                "Inhale dust",
                "Repot a plant that's gotten too big",
                "Slide on the floors with your socks",
                "Go to a friends place that has hardwood floors",
                "Throw away your old school books that you don't use",
                "Print off a photo and stick it in a frame",
                "See how many cups of tea you can make out of one teabag",
                "Trim your nails",
                "Paint your nails",
                "Do five pushups",
                "Write a letter",
                "Email your local council member",
                "Check all your glasses for chips and cracks",
                "Water the plants",
                "Have a cup of tea",
                "Make passata sauce",
                "Throw out old condiments in your fridge",
                "Walk around the block",
                "Watch videos of deep sea creatures",
                "Clean your hiking boots",
                "Message a friend who lives overseas",
                "Update your car insurance",
                "Pair your socks",
                "Read a cookbook you have laying around",
                "Throw out old vitamins",
                "Go through a box you haven't opened since you moved in"
        )

        return things.random()
    }

    companion object {
        /**
         * Whether or not the system UI should be auto-hidden after
         * [AUTO_HIDE_DELAY_MILLIS] milliseconds.
         */
        private const val AUTO_HIDE = true

        /**
         * If [AUTO_HIDE] is set, the number of milliseconds to wait after
         * user interaction before hiding the system UI.
         */
        private const val AUTO_HIDE_DELAY_MILLIS = 3000

        /**
         * Some older devices needs a small delay between UI widget updates
         * and a change of the status and navigation bar.
         */
        private const val UI_ANIMATION_DELAY = 300
    }
}