package product.com.play;

import android.Manifest;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class PlayActivity extends AppCompatActivity implements View.OnClickListener, Runnable {

    private Drawable zero, cross;
    private ImageView[][] img = new ImageView[3][3];
    private TextView txtPlayer1, txtPlayer2, txtScore;
    private LinearLayout layout1, layout2;
    private int player = 0;
    private int red = Color.RED, green = Color.GREEN;
    private int[][] M;
    private MediaPlayer playAudioTing;
    private MediaPlayer playAudioWin;
    private Vibrator vib;
    private Animation animMark, animSlideA, animSlideB, animMatchPattern;
    private ImageView imgWin, imgLose;
    private Toast toastWin, toastLose;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.VIBRATE}, 1);

        img[0][0] = findViewById(R.id.img00);
        img[0][1] = findViewById(R.id.img01);
        img[0][2] = findViewById(R.id.img02);
        img[1][0] = findViewById(R.id.img10);
        img[1][1] = findViewById(R.id.img11);
        img[1][2] = findViewById(R.id.img12);
        img[2][0] = findViewById(R.id.img20);
        img[2][1] = findViewById(R.id.img21);
        img[2][2] = findViewById(R.id.img22);

        txtPlayer1 = findViewById(R.id.txt_player_1);
        txtPlayer2 = findViewById(R.id.txt_player_2);
        txtScore = findViewById(R.id.score);

        layout1 = findViewById(R.id.activity_main);
        layout2 = findViewById(R.id.activity_main1);

        img[0][0].setOnClickListener(this);
        img[0][1].setOnClickListener(this);
        img[0][2].setOnClickListener(this);
        img[1][0].setOnClickListener(this);
        img[1][1].setOnClickListener(this);
        img[1][2].setOnClickListener(this);
        img[2][0].setOnClickListener(this);
        img[2][1].setOnClickListener(this);
        img[2][2].setOnClickListener(this);

        animMark = AnimationUtils.loadAnimation(this, R.anim.mark_anim);
        animSlideA = AnimationUtils.loadAnimation(this, R.anim.next_game_a);
        animSlideB = AnimationUtils.loadAnimation(this, R.anim.next_game_b);
        animMatchPattern = AnimationUtils.loadAnimation(this, R.anim.anim_match_pattern);

        vib = (Vibrator) getSystemService(VIBRATOR_SERVICE);

        playAudioTing = MediaPlayer.create(this, R.raw.ting);
        playAudioWin = MediaPlayer.create(this, R.raw.newgame);

        imgWin = new ImageView(this);
        imgLose = new ImageView(this);
        imgWin.setImageResource(R.mipmap.win);
        imgLose.setImageResource(R.mipmap.lost);

        toastWin = new Toast(this);
        toastLose = new Toast(this);

        toastWin.setView(imgWin);
        toastLose.setView(imgLose);

        toastWin.setDuration(Toast.LENGTH_SHORT);
        toastLose.setDuration(Toast.LENGTH_SHORT);

        zero = ContextCompat.getDrawable(this, R.mipmap.zero);
        cross = ContextCompat.getDrawable(this, R.mipmap.cross);

        resetGame();
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.img00:

                putMark(img[0][0], 0, 0);
                break;
            case R.id.img01:

                putMark(img[0][1], 0, 1);
                break;
            case R.id.img02:

                putMark(img[0][2], 0, 2);
                break;
            case R.id.img10:

                putMark(img[1][0], 1, 0);
                break;
            case R.id.img11:

                putMark(img[1][1], 1, 1);
                break;
            case R.id.img12:

                putMark(img[1][2], 1, 2);
                break;
            case R.id.img20:

                putMark(img[2][0], 2, 0);
                break;
            case R.id.img21:

                putMark(img[2][1], 2, 1);
                break;
            case R.id.img22:

                putMark(img[2][2], 2, 2);
                break;
        }
    }

    private void putMark(ImageView imageView, int x, int y) {

        if (M[x][y] != -1) {

            Toast.makeText(this, "Already Marked", Toast.LENGTH_SHORT).show();
            return;
        }
        imageView.setImageDrawable(player == 0 ? zero : cross);          // Put mark on UI box.
        playAudioTing.start();
        imageView.startAnimation(animMark);
        M[x][y] = player;                                                // Set mark in array.
        player = player == 0 ? 1 : 0;                                      // Check if won.
        checkResult();
    }

    private void checkResult() {

        boolean gameWon = true;
        if ((M[0][0] == M[0][1]) && (M[0][0] == M[0][2]) && (M[0][0] != -1)) {

            img[0][0].startAnimation(animMatchPattern);
            img[0][1].startAnimation(animMatchPattern);
            img[0][2].startAnimation(animMatchPattern);

        } else if ((M[1][0] == M[1][1]) && (M[1][0] == M[1][2]) && (M[1][0] != -1)) {

            img[1][0].startAnimation(animMatchPattern);
            img[1][1].startAnimation(animMatchPattern);
            img[1][2].startAnimation(animMatchPattern);

        } else if ((M[2][0] == M[2][1]) && (M[2][0] == M[2][2]) && (M[2][0] != -1)) {

            img[2][0].startAnimation(animMatchPattern);
            img[2][1].startAnimation(animMatchPattern);
            img[2][2].startAnimation(animMatchPattern);

        } else if ((M[0][0] == M[1][0]) && (M[0][0] == M[2][0]) && (M[0][0] != -1)) {

            img[0][0].startAnimation(animMatchPattern);
            img[1][0].startAnimation(animMatchPattern);
            img[2][0].startAnimation(animMatchPattern);

        } else if ((M[0][1] == M[1][1]) && (M[0][1] == M[2][1]) && (M[0][1] != -1)) {

            img[0][1].startAnimation(animMatchPattern);
            img[1][1].startAnimation(animMatchPattern);
            img[2][1].startAnimation(animMatchPattern);

        } else if ((M[0][2] == M[1][2]) && (M[0][2] == M[2][2]) && (M[0][2] != -1)) {

            img[0][2].startAnimation(animMatchPattern);
            img[1][2].startAnimation(animMatchPattern);
            img[2][2].startAnimation(animMatchPattern);

        } else if ((M[0][0] == M[1][1]) && (M[0][0] == M[2][2]) && (M[0][0] != -1)) {

            img[0][0].startAnimation(animMatchPattern);
            img[1][1].startAnimation(animMatchPattern);
            img[2][2].startAnimation(animMatchPattern);

        } else if ((M[1][1] == M[2][0]) && (M[1][1] == M[0][2]) && (M[1][1] != -1)) {

            img[2][0].startAnimation(animMatchPattern);
            img[1][1].startAnimation(animMatchPattern);
            img[0][2].startAnimation(animMatchPattern);

        } else {

            gameWon = false;
        }

        if (gameWon) {

            new Handler().postDelayed(this, 1500);
        }
    }

    private void resetGame() {

        player = 0;
        M = new int[][]{{-1, -1, -1}, {-1, -1, -1}, {-1, -1, -1}};
        img[0][0].setImageResource(android.R.color.transparent);
        img[0][1].setImageResource(android.R.color.transparent);
        img[0][2].setImageResource(android.R.color.transparent);
        img[1][0].setImageResource(android.R.color.transparent);
        img[1][1].setImageResource(android.R.color.transparent);
        img[1][2].setImageResource(android.R.color.transparent);
        img[2][0].setImageResource(android.R.color.transparent);
        img[2][1].setImageResource(android.R.color.transparent);
        img[2][2].setImageResource(android.R.color.transparent);

        layout1.startAnimation(animSlideA);
        layout2.startAnimation(animSlideB);
        txtPlayer1.startAnimation(AnimationUtils.loadAnimation(this, R.anim.appear_player));
        txtPlayer2.startAnimation(AnimationUtils.loadAnimation(this, R.anim.appear_player));
    }

    @Override
    public void run() {

        playAudioWin.start();
        vib.vibrate(70);
        resetGame();
        toastWin.show();
    }
}
