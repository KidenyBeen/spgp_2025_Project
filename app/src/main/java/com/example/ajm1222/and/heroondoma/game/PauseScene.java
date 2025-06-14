package com.example.ajm1222.and.heroondoma.game;

import android.app.AlertDialog;
import android.content.DialogInterface;

import com.example.ajm1222.and.a2dg.framework.objects.Button;
import com.example.ajm1222.and.a2dg.framework.objects.Score;
import com.example.ajm1222.and.a2dg.framework.objects.Sprite;
import com.example.ajm1222.and.a2dg.framework.scene.Scene;
import com.example.ajm1222.and.a2dg.framework.view.GameView;
import com.example.ajm1222.and.a2dg.framework.view.Metrics;
import com.example.ajm1222.and.heroondoma.R;

public class PauseScene extends Scene
{

    private final Score score;
    private boolean scoreType; //모드에 맞춰 조건을 맞추고 종료했는지 혹은 눌러서 종료했는지 구분

    public enum Layer {
        bg, ui,title, touch
    }

    public PauseScene(GameTimer timer, Score score , boolean type) {
        initLayers(Layer.values().length);
        float w = Metrics.width, h = Metrics.height;

        this.score = new Score(R.mipmap.number_24x32, w/2 + 30, h/2, 60f);
        this.score.setScore(score.getScore());

        add(Layer.bg, new Sprite(R.mipmap.trans_50b, w/2,h/2,w,2000f));
        add(Layer.bg, new Sprite(R.mipmap.point, w/2,h/3,600f,300f));
        add(Layer.ui, this.score);
        add(Layer.touch, new Button(R.mipmap.btn_resume_n, 200f, Metrics.height, 200f, 75f, new Button.OnTouchListener() {
            @Override
            public boolean onTouch(boolean pressed) {

                if(pressed && type)
                {
                    pop();
                }
                return false;
            }
        }));
        add(Layer.touch, new Button(R.mipmap.btn_exit_n, Metrics.width - 267f, Metrics.height, 267f, 100f, new Button.OnTouchListener() {
            @Override
            public boolean onTouch(boolean pressed) {
                if(pressed) {
                    new AlertDialog.Builder(GameView.view.getContext())
                            .setTitle("Confirm")
                            .setMessage("Do you really want to exit the game?")
                            .setNegativeButton("No", null)
                            .setPositiveButton("Exit", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    popAll();
                                }
                            })
                            .create()
                            .show();
                }
                return false;
            }
        }));
    }

    @Override
    protected int getTouchLayerIndex() {
        return Layer.touch.ordinal();
    }

    // Overridables
    @Override
    public boolean isTransparent() {
        return true;
    }
}
