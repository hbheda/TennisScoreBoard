package com.harshit.tennisscoreboard

import android.graphics.Typeface
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.harshit.tennisscoreboard.databinding.ActivityMainBinding
import java.util.*

class MainActivity : AppCompatActivity() {

    private val TAG: String = MainActivity::class.java.simpleName
    private lateinit var binding: ActivityMainBinding


    private var gameStarted = false
    private var deuceGame = false
    private var tieBreaker = false

    private var mIsMatchCurrent = true
    private var mIsMatchOld = false

    private var servePlayer = 0
    private var playerOpenSet = 0
    private var currentSet = 0

    private var totalSets = 0
    private var setsToWin = 0

    private var harshitScoreBoard: ArrayList<TextView>? = null
    private var bhedaScoreBoard: ArrayList<TextView>? = null

    private var harshitGameScoreList: ArrayList<TextView>? = null
    private var bhedaGameScoreList: ArrayList<TextView>? = null

    private val NEW_LINE = "\n"
    private val COMMA_SPACE = ", "

    private val gameSetPoint0 = 0

    private var gamePoint0: String = "0"
    private var gamePointLove: String = "love"
    private var gamePoint15: String = "15"
    private var gamePoint30: String = "30"
    private var gamePoint40: String = "40"
    private var gamePointAd: String = "Ad"
    private var gamePoint60: String = "60"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root/*R.layout.activity_main*/)

        onMatchTypeButtonClicked(findViewById(R.id.mens_rbtn))

        if (savedInstanceState == null) {

            updateGameInfo(getString(R.string.game_play_text_welcome))

            enableWinButtons(false)
        }
    }

    private fun enableWinButtons(enabled: Boolean) {
        binding.btnHarshitWin.isEnabled = enabled
        binding.btnBhedaWin.isEnabled = enabled
    }


    private fun insertScoreData() {

        harshitScoreBoard = ArrayList(totalSets + 1)
        harshitGameScoreList = ArrayList(3)
        if (mIsMatchOld) {
            harshitScoreBoard!!.add(binding.txtHarSetPoint)
            harshitScoreBoard!!.add(binding.harRound1Point)
            harshitScoreBoard!!.add(binding.harRound2Point)
            harshitScoreBoard!!.add(binding.harRound3Point)
        } else if (mIsMatchCurrent) {
            harshitScoreBoard!!.add(binding.txtHarSetPoint)
            harshitScoreBoard!!.add(binding.harRound1Point)
            harshitScoreBoard!!.add(binding.harRound2Point)
            harshitScoreBoard!!.add(binding.harRound3Point)
            harshitScoreBoard!!.add(binding.harRound4Point)
            harshitScoreBoard!!.add(binding.harRound5Point)
        }
        harshitGameScoreList!!.add(binding.p1GamePtsText)
        harshitGameScoreList!!.add(binding.p1GameplayPtsText)
        harshitGameScoreList!!.add(binding.p1TbPtsText)



        bhedaScoreBoard = ArrayList(totalSets + 1)
        bhedaGameScoreList = ArrayList(3)
        if (mIsMatchOld) {
            bhedaScoreBoard!!.add(binding.txtBheSetPoint)
            bhedaScoreBoard!!.add(binding.bheRound1Point)
            bhedaScoreBoard!!.add(binding.bheRound2Point)
            bhedaScoreBoard!!.add(binding.bheRound3Point)
        } else if (mIsMatchCurrent) {
            bhedaScoreBoard!!.add(binding.txtBheSetPoint)
            bhedaScoreBoard!!.add(binding.bheRound1Point)
            bhedaScoreBoard!!.add(binding.bheRound2Point)
            bhedaScoreBoard!!.add(binding.bheRound3Point)
            bhedaScoreBoard!!.add(binding.bheRound4Point)
            bhedaScoreBoard!!.add(binding.bheRound5Point)
        }
        bhedaGameScoreList!!.add(binding.bheGameScore)
        bhedaGameScoreList!!.add(binding.bheGamePoint)
        bhedaGameScoreList!!.add(binding.bhedaTbPoint)
    }


    fun onStartButton(view: View?) {
        if (gameStarted) {

            clearDataGrp()

            enableMatchGrp(true)

            resetScores()

            resetGameMatchPoint()

            changeBeginButtonText(getString(R.string.start_match))

            updateGameInfo(getString(R.string.game_play_text_welcome))

            gameStarted = false
            tieBreaker = false
            deuceGame = false
            mIsMatchOld = false

            mIsMatchCurrent = true


            mIsMatchCurrent = true
            servePlayer = 0


            currentPlayer()


            harshitScoreBoard!!.clear()
            harshitScoreBoard = null
            harshitGameScoreList!!.clear()
            harshitGameScoreList = null
            bhedaScoreBoard!!.clear()
            bhedaScoreBoard = null
            bhedaGameScoreList!!.clear()
            bhedaGameScoreList = null


            enableWinButtons(false)
        } else {

            if (!mIsMatchCurrent && !mIsMatchOld) {
                updateGameInfo(getString(R.string.game_play_text_select_match_type))
            } else {

                playerOpenSet = 1
                servePlayer = playerOpenSet

                currentPlayer()
                updateGameInfo(getString(R.string.game_play_text_toss, playerOpenSet))

                enableMatchGrp(false)

                changeBeginButtonText(getString(R.string.reset_match))

                insertScoreData()

                currentSet = 1

                enableWinButtons(true)

                gameStarted = true
            }
        }
    }


    private fun changeBeginButtonText(buttonTextMsg: String?) {
        binding.btnStartGame.text = buttonTextMsg
    }

    private fun updateGameInfo(message: String?, append: Boolean) {
        val gamePlayTextView: TextView = findViewById(R.id.game_play_text)
        if (append) {

            gamePlayTextView.text =
                TextUtils.concat(gamePlayTextView.text.toString(), NEW_LINE, message)
        } else {

            gamePlayTextView.text = message
        }
    }


    private fun updateGameInfo(message: String?) {
        updateGameInfo(message, false)
    }

    fun onMatchTypeButtonClicked(view: View) {

        //Is the match type selected
        val isChecked = (view as RadioButton).isChecked
        when (view.getId()) {
            R.id.mens_rbtn -> if (isChecked) {
                mIsMatchCurrent = true
                mIsMatchOld = false
                totalSets = 5
                setsToWin = (totalSets + 1) / 2
            }
            R.id.womens_rbtn -> if (isChecked) {
                mIsMatchOld = true
                mIsMatchCurrent = false
                totalSets = 3
                setsToWin = (totalSets + 1) / 2
            }
        }

    }


    private fun enableMatchGrp(enabled: Boolean) {
        val mensRadioButton: RadioButton = findViewById(R.id.mens_rbtn)
        val womensRadioButton: RadioButton = findViewById(R.id.womens_rbtn)
        mensRadioButton.isEnabled = enabled
        womensRadioButton.isEnabled = enabled
    }


    private fun clearDataGrp() {
        val matchTypeRadioGroup: RadioGroup = findViewById(R.id.match_type_rbtn_grp)
        matchTypeRadioGroup.clearCheck()
    }

    private fun currentPlayer() {
        if (servePlayer == 1) {

            val p1ScoreBoardText: TextView = findViewById(R.id.player_harshit)
            p1ScoreBoardText.setTypeface(
                Typeface.create(
                    p1ScoreBoardText.typeface,
                    Typeface.NORMAL
                ), Typeface.BOLD
            )


            val p2ScoreBoardText: TextView = findViewById(R.id.player_bheda)
            p2ScoreBoardText.setTypeface(
                Typeface.create(
                    p2ScoreBoardText.typeface,
                    Typeface.NORMAL
                ), Typeface.NORMAL
            )

        } else if (servePlayer == 2) {



            val p2ScoreBoardText: TextView = findViewById(R.id.player_bheda)
            p2ScoreBoardText.setTypeface(
                Typeface.create(
                    p2ScoreBoardText.typeface,
                    Typeface.NORMAL
                ), Typeface.BOLD
            )


            val p1ScoreBoardText: TextView = findViewById(R.id.player_harshit)
            p1ScoreBoardText.setTypeface(
                Typeface.create(
                    p1ScoreBoardText.typeface,
                    Typeface.NORMAL
                ), Typeface.NORMAL
            )

        } else if (servePlayer == 0) {

            val p1ScoreBoardText: TextView = findViewById(R.id.player_harshit)
            p1ScoreBoardText.setTypeface(
                Typeface.create(
                    p1ScoreBoardText.typeface,
                    Typeface.NORMAL
                ), Typeface.NORMAL
            )

            val p2ScoreBoardText: TextView = findViewById(R.id.player_bheda)
            p2ScoreBoardText.setTypeface(
                Typeface.create(
                    p2ScoreBoardText.typeface,
                    Typeface.NORMAL
                ), Typeface.NORMAL
            )

        }
    }

    private fun getNextGamePlayPoint(currentPointStr: String): String? {
        if (currentPointStr == gamePoint0) {
            return gamePoint15
        } else if (currentPointStr == gamePoint15) {
            return gamePoint30
        } else if (currentPointStr == gamePoint30) {
            return gamePoint40
        } else if (currentPointStr == gamePoint40 && deuceGame) {
            return gamePointAd
        } else if (currentPointStr == gamePoint40) {
            return gamePoint60
        } else if (currentPointStr == gamePointAd) {
            deuceGame = false //Updating to false as Advantage is won by the player
            return gamePoint60
        }
        return gamePoint0 //Returning 0 by default
    }


    fun onPlusButtonClicked(view: View) {
        if (!tieBreaker) {

            val p1GamePlayTextView = harshitGameScoreList!![1]
            val p1CurrentGamePlayPoint = p1GamePlayTextView.text.toString()
            val p2GamePlayTextView = bhedaGameScoreList!![1]

            val p2CurrentGamePlayPoint = p2GamePlayTextView.text.toString()
            when (view.id) {
                R.id.btn_harshit_win -> {
                    val p1NextPointStr = getNextGamePlayPoint(p1CurrentGamePlayPoint)
                    if (p1NextPointStr == gamePoint60) {

                        p1GamePlayTextView.text = gamePoint0
                        p2GamePlayTextView.text = gamePoint0

                        servePlayer = servePlayer % 2 + 1
                        currentPlayer()

                        updateGamePoints(1)

                        showDialog("Match Won", "Harshit Won the Match")
                        //Toast.makeText(this,"Harshit Won the Match",Toast.LENGTH_SHORT).show()

                    } else if (p1NextPointStr == gamePoint40 && p1NextPointStr == p2CurrentGamePlayPoint) {

                        deuceGame = true

                        p1GamePlayTextView.text = p1NextPointStr

                        updateGameInfo(
                            getString(
                                R.string.game_play_text_deuce,
                                servePlayer,
                                currentSet
                            )
                        )
                    } else if (p1NextPointStr == gamePointAd && p1NextPointStr == p2CurrentGamePlayPoint) {

                        p1GamePlayTextView.text = gamePoint40
                        p2GamePlayTextView.text = gamePoint40

                        updateGameInfo(
                            getString(
                                R.string.game_play_text_deuce_repeat,
                                servePlayer,
                                currentSet
                            )
                        )
                    } else {
                        p1GamePlayTextView.text = p1NextPointStr

                        if (p1NextPointStr == gamePointAd) {
                            //If Player 1 wins the Advantage
                            updateGameInfo(
                                getString(
                                    R.string.game_play_text_advantage,
                                    servePlayer,
                                    currentSet,
                                    1
                                )
                            )
                        } else {
                            //For all other points
                            if (servePlayer == 1) {
                                updateGameInfo(
                                    getString(
                                        R.string.game_play_text_gameplay_point,
                                        servePlayer,
                                        currentSet,
                                        p1NextPointStr,
                                        if (p2CurrentGamePlayPoint == gamePoint0) gamePointLove else p2CurrentGamePlayPoint
                                    )
                                )
                            } else if (servePlayer == 2) {
                                updateGameInfo(
                                    getString(
                                        R.string.game_play_text_gameplay_point,
                                        servePlayer,
                                        currentSet,
                                        if (p2CurrentGamePlayPoint == gamePoint0) gamePointLove else p2CurrentGamePlayPoint,
                                        p1NextPointStr
                                    )
                                )
                            }
                        }
                    }
                }
                R.id.btn_bheda_win -> {
                    val p2NextPointStr = getNextGamePlayPoint(p2CurrentGamePlayPoint)
                    if (p2NextPointStr == gamePoint60) {

                        p2GamePlayTextView.text = gamePoint0
                        p1GamePlayTextView.text = gamePoint0

                        servePlayer = servePlayer % 2 + 1
                        currentPlayer()


                        updateGamePoints(2)
                        showDialog("Match Won", "Bheda Won the Match")
                    //Toast.makeText(this,"Bheda Won the Match",Toast.LENGTH_SHORT).show()
                    } else if (p2NextPointStr == gamePoint40 && p2NextPointStr == p1CurrentGamePlayPoint) {

                        deuceGame = true

                        updateGameInfo(
                            getString(
                                R.string.game_play_text_deuce,
                                servePlayer,
                                currentSet
                            )
                        )
                    } else if (p2NextPointStr == gamePointAd && p2NextPointStr == p1CurrentGamePlayPoint) {

                        p2GamePlayTextView.text = gamePoint40
                        p1GamePlayTextView.text = gamePoint40

                        updateGameInfo(
                            getString(
                                R.string.game_play_text_deuce_repeat,
                                servePlayer,
                                currentSet
                            )
                        )
                    } else {

                        p2GamePlayTextView.text = p2NextPointStr

                        if (p2NextPointStr == gamePointAd) {
                            //If Player 2 wins the Advantage
                            updateGameInfo(
                                getString(
                                    R.string.game_play_text_advantage,
                                    servePlayer,
                                    currentSet,
                                    2
                                )
                            )
                        } else {
                            //For all other points
                            if (servePlayer == 2) {
                                updateGameInfo(
                                    getString(
                                        R.string.game_play_text_gameplay_point,
                                        servePlayer,
                                        currentSet,
                                        p2NextPointStr,
                                        if (p1CurrentGamePlayPoint == gamePoint0) gamePointLove else p1CurrentGamePlayPoint
                                    )
                                )
                            } else if (servePlayer == 1) {
                                updateGameInfo(
                                    getString(
                                        R.string.game_play_text_gameplay_point,
                                        servePlayer,
                                        currentSet,
                                        if (p1CurrentGamePlayPoint == gamePoint0) gamePointLove else p1CurrentGamePlayPoint,
                                        p2NextPointStr
                                    )
                                )
                            }
                        }
                    }
                }
            }
        } else {

            val p1GamePointTextView = harshitGameScoreList!![0]

            var p1GamePoints = p1GamePointTextView.text.toString().toInt()
            val p2GamePointTextView = bhedaGameScoreList!![0]

            var p2GamePoints = p2GamePointTextView.text.toString().toInt()
            val p1GamePlayTextView = harshitGameScoreList!![2]

            var p1TieBreakerPoints = p1GamePlayTextView.text.toString().toInt()
            val p2GamePlayTextView = bhedaGameScoreList!![2]
            var p2TieBreakerPoints = p2GamePlayTextView.text.toString().toInt()


            val currentSetP1ScoreboardTextView = harshitScoreBoard!![currentSet]
            val currentSetP2ScoreboardTextView = bhedaScoreBoard!![currentSet]
            when (view.id) {
                R.id.btn_harshit_win -> {
                    p1TieBreakerPoints += 1
                    p1GamePlayTextView.text = p1TieBreakerPoints.toString()
                    //Updating Scoreboard of Player - 1
                    currentSetP1ScoreboardTextView.text =
                        getString(
                            R.string.game_score_tiebreaker_format,
                            p1GamePoints,
                            p1TieBreakerPoints
                        )
                }
                R.id.btn_bheda_win -> {
                    p2TieBreakerPoints += 1
                    p2GamePlayTextView.text = p2TieBreakerPoints.toString()
                    //Updating Scoreboard of Player - 2
                    currentSetP2ScoreboardTextView.text = getString(
                        R.string.game_score_tiebreaker_format,
                        p2GamePoints,
                        p2TieBreakerPoints
                    )
                }
            }

            val totalPointsReached = p1TieBreakerPoints + p2TieBreakerPoints

            //Highlighting the player who serves next
            if ((totalPointsReached - 1) % 2 == 0) {
                servePlayer = servePlayer % 2 + 1
                currentPlayer()
            }

            //Updating the Game play text
            if (servePlayer == 1) {
                updateGameInfo(
                    getString(
                        R.string.game_play_text_tiebreaker_point, servePlayer, currentSet,
                        p1TieBreakerPoints, p2TieBreakerPoints
                    )
                )
            } else if (servePlayer == 2) {
                updateGameInfo(
                    getString(
                        R.string.game_play_text_tiebreaker_point, servePlayer, currentSet,
                        p2TieBreakerPoints, p1TieBreakerPoints
                    )
                )
            }


            val tieBreakerPointDifference = Math.abs(p1TieBreakerPoints - p2TieBreakerPoints)

            val maxPointsReached = Math.max(p1TieBreakerPoints, p2TieBreakerPoints)
            if (maxPointsReached >= 7 && tieBreakerPointDifference >= 2) {

                if (p1TieBreakerPoints == maxPointsReached) {

                    p1GamePointTextView.text = (++p1GamePoints).toString()

                    currentSetP1ScoreboardTextView.text =
                        getString(
                            R.string.game_score_tiebreaker_format,
                            p1GamePoints,
                            p1TieBreakerPoints
                        )

                    updateSetPoints(1)
                } else if (p2TieBreakerPoints == maxPointsReached) {

                    p2GamePointTextView.text = (++p2GamePoints).toString()

                    currentSetP2ScoreboardTextView.text =
                        getString(
                            R.string.game_score_tiebreaker_format,
                            p2GamePoints,
                            p2TieBreakerPoints
                        )

                    updateSetPoints(2)
                }
            }
        }
    }

    private fun updateGamePoints(player: Int) {
        val p1GamePointTextView = harshitGameScoreList!![0]

        var p1GamePoints = p1GamePointTextView.text.toString().toInt()
        val p2GamePointTextView = bhedaGameScoreList!![0]

        var p2GamePoints = p2GamePointTextView.text.toString().toInt()


        if (player == 1) {
            p1GamePoints += 1
            p1GamePointTextView.text = p1GamePoints.toString()
            val currentSetP1ScoreboardTextView = harshitScoreBoard!![currentSet]
            currentSetP1ScoreboardTextView.text = p1GamePoints.toString()
        } else if (player == 2) {
            p2GamePoints += 1
            p2GamePointTextView.text = p2GamePoints.toString()
            val currentSetP2ScoreboardTextView = bhedaScoreBoard!![currentSet]
            currentSetP2ScoreboardTextView.text = p2GamePoints.toString()
        }


        if (servePlayer == 1) {
            updateGameInfo(
                getString(
                    R.string.game_play_text_game_point,
                    servePlayer,
                    currentSet,
                    if (p1GamePoints == gameSetPoint0) gamePointLove else p1GamePoints.toString(),
                    if (p2GamePoints == gameSetPoint0) gamePointLove else p2GamePoints.toString()
                )
            )
        } else if (servePlayer == 2) {
            updateGameInfo(
                getString(
                    R.string.game_play_text_game_point,
                    servePlayer,
                    currentSet,
                    if (p2GamePoints == gameSetPoint0) gamePointLove else p2GamePoints.toString(),
                    if (p1GamePoints == gameSetPoint0) gamePointLove else p1GamePoints.toString()
                )
            )
        }


        val gamePointDifference = Math.abs(p1GamePoints - p2GamePoints)

        val maxPointsReached = Math.max(p1GamePoints, p2GamePoints)


        if (currentSet != totalSets) {

            if (maxPointsReached >= 6 && gamePointDifference >= 2) {

                if (p1GamePoints == maxPointsReached) {

                    updateSetPoints(1)
                } else if (p2GamePoints == maxPointsReached) {

                    updateSetPoints(2)
                }
            } else if (maxPointsReached == 6 && gamePointDifference == 0) {

                tieBreaker = true



                updateGameInfo(
                    getString(
                        R.string.game_play_text_tiebreaker_start,
                        currentSet,
                        servePlayer
                    ), true
                )
            }
        } else {

            if (maxPointsReached >= 6 && gamePointDifference >= 2) {

                if (p1GamePoints == maxPointsReached) {

                    updateSetPoints(1)
                } else if (p2GamePoints == maxPointsReached) {

                    updateSetPoints(2)
                }
            }
        }

    }

    private fun updateSetPoints(player: Int) {
        val p1SetPtsScoreboardTextView = harshitScoreBoard!![0]

        var p1TotalSetPts = p1SetPtsScoreboardTextView.text.toString().toInt()
        val p2SetPtsScoreboardTextView = bhedaScoreBoard!![0]

        var p2TotalSetPts = p2SetPtsScoreboardTextView.text.toString().toInt()

        if (player == 1) {
            p1SetPtsScoreboardTextView.text = (++p1TotalSetPts).toString()
            showDialog("Set Won", "Harshit Won the Set")
        } else if (player == 2) {
            p2SetPtsScoreboardTextView.text = (++p2TotalSetPts).toString()
            showDialog("Set Won", "Bheda Won the Set")
        }



        val currentSetP1ScoreboardTextView = harshitScoreBoard!![currentSet]
        val p1GamePointStr = currentSetP1ScoreboardTextView.text.toString()


        val currentSetP2ScoreboardTextView = bhedaScoreBoard!![currentSet]
        val p2GamePointStr = currentSetP2ScoreboardTextView.text.toString()


        if (player == 1) {

            updateGameInfo(
                getString(
                    R.string.game_play_text_set_point,
                    currentSet,
                    player,
                    p1GamePointStr,
                    p2GamePointStr
                ), true
            )
        } else if (player == 2) {

            updateGameInfo(
                getString(
                    R.string.game_play_text_set_point,
                    currentSet,
                    player,
                    p2GamePointStr,
                    p1GamePointStr
                ), true
            )
        }


        val maxPointsReached = Math.max(p1TotalSetPts, p2TotalSetPts)
        if (maxPointsReached == setsToWin) {


            if (p1TotalSetPts == maxPointsReached) {

                updateGameInfo(
                    getString(
                        R.string.game_play_text_match_point,
                        1,
                        2,
                        getMatchScore(1)
                    ), true
                )
                showDialog("Game Won", "Harshit Won the Game")
            } else if (p2TotalSetPts == maxPointsReached) {

                updateGameInfo(
                    getString(
                        R.string.game_play_text_match_point,
                        2,
                        1,
                        getMatchScore(2)
                    ), true
                )
                showDialog("Game Won", "Bheda Won the Game")
            }


            enableWinButtons(false)


            changeBeginButtonText(getString(R.string.play_again))
        } else if (maxPointsReached < setsToWin) {


            resetGameMatchPoint()
            if (tieBreaker) {
                tieBreaker = false
            }

            currentSet += 1

            playerOpenSet = playerOpenSet % 2 + 1
            servePlayer = playerOpenSet

            currentPlayer()

            if (servePlayer == 1) {
                updateGameInfo(
                    getString(
                        R.string.game_play_text_next_set,
                        servePlayer,
                        if (p1TotalSetPts == gameSetPoint0) gamePointLove else p1TotalSetPts.toString(),
                        if (p2TotalSetPts == gameSetPoint0) gamePointLove else p2TotalSetPts.toString()
                    ), true
                )
            } else if (servePlayer == 2) {
                updateGameInfo(
                    getString(
                        R.string.game_play_text_next_set,
                        servePlayer,
                        if (p2TotalSetPts == gameSetPoint0) gamePointLove else p2TotalSetPts.toString(),
                        if (p1TotalSetPts == gameSetPoint0) gamePointLove else p1TotalSetPts.toString()
                    ), true
                )
            }
        }
    }



    private fun resetGameMatchPoint() {

        for (gamePlayTextView in harshitGameScoreList!!) {
            gamePlayTextView.text = gamePoint0
        }

        for (gamePlayTextView in bhedaGameScoreList!!) {
            gamePlayTextView.text = gamePoint0
        }
    }


    private fun resetScores() {

        for (scoreboardTextView in harshitScoreBoard!!) {
            scoreboardTextView.text = gamePoint0
        }

        for (scoreboardTextView in bhedaScoreBoard!!) {
            scoreboardTextView.text = gamePoint0
        }
    }


    private fun getMatchScore(player: Int): String {
        val scoreBuilder = StringBuilder()
        for (index in 1..currentSet) {
            val p1CurrentIndexSetTextView = harshitScoreBoard!![index]
            val p2CurrentIndexSetTextView = bhedaScoreBoard!![index]
            if (player == 1) {
                scoreBuilder.append(
                    getString(
                        R.string.game_score_format,
                        p1CurrentIndexSetTextView.text.toString(),
                        p2CurrentIndexSetTextView.text.toString()
                    )
                )
            } else if (player == 2) {

                scoreBuilder.append(
                    getString(
                        R.string.game_score_format,
                        p2CurrentIndexSetTextView.text.toString(),
                        p1CurrentIndexSetTextView.text.toString()
                    )
                )
            }
            if (index < currentSet) {
                scoreBuilder.append(COMMA_SPACE)
            }
        }
        return scoreBuilder.toString()
    }

    private fun showDialog(title: String, description: String){
        val builder = AlertDialog.Builder(this)
        builder.setTitle(title)
        builder.setMessage(description)
        builder.setPositiveButton("OK"){ dialog, _ -> dialog.dismiss() }

        builder.show()
    }

}
