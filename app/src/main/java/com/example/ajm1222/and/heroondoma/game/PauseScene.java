package com.example.ajm1222.and.heroondoma.game;

import android.app.AlertDialog;
import android.content.DialogInterface;

import com.example.ajm1222.and.a2dg.framework.objects.Button;
import com.example.ajm1222.and.a2dg.framework.scene.Scene;
import com.example.ajm1222.and.a2dg.framework.view.GameView;
import com.example.ajm1222.and.a2dg.framework.view.Metrics;
import com.example.ajm1222.and.heroondoma.R;

public class PauseScene extends Scene
{
    public enum Layer {
        bg, title, touch
    }
    protected float angle = -(float)Math.PI / 2;
    public PauseScene() {
        initLayers(Layer.values().length);
        float w = Metrics.width, h = Metrics.height;

        add(Layer.touch, new Button(R.mipmap.btn_resume_n, 400f, 100f, 200f, 75f, new Button.OnTouchListener() {
            @Override
            public boolean onTouch(boolean pressed) {

                if(pressed)
                {
                    pop();
                }
                return false;
            }
        }));
        add(Layer.touch, new Button(R.mipmap.btn_exit_n, 400f, 550f, 267f, 100f, new Button.OnTouchListener() {
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
