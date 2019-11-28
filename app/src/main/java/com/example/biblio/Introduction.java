package com.example.biblio;

import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;

import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntroFragment;
import com.github.paolorotolo.appintro.model.SliderPage;

public class Introduction extends AppIntro {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        createSlides();
        setNavBarColor(R.color.colorPrimaryDark);
        setSeparatorColor(getResources().getColor(R.color.white));
        showSkipButton(false);

        Button mDoneBtn = findViewById(R.id.done);
        mDoneBtn.setBackgroundColor(Color.TRANSPARENT);
        mDoneBtn.setText(getResources().getText(R.string.done_msg));
        mDoneBtn.setAllCaps(false);

        setProgressButtonEnabled(true);
        setFadeAnimation();
    }

    private void createSlides(){
        Resources resources = getResources();
        createSlideFragment("Search facility", resources.getString(R.string.introduction_search), R.drawable.search, resources.getColor(R.color.app_background));
        createSlideFragment("Store books", resources.getString(R.string.introduction_download), R.drawable.cloud_download, resources.getColor(R.color.secondary));
        createSlideFragment("Refresh facility", resources.getString(R.string.introduction_refresh), R.drawable.swipe_down, resources.getColor(R.color.primary));
    }

    private void createSlideFragment(String title, String description, int image, int background) {
        SliderPage sliderPage = new SliderPage();
        sliderPage.setTitle(title);
        sliderPage.setDescription(description);
        sliderPage.setImageDrawable(image);
        sliderPage.setBgColor(background);
        addSlide(AppIntroFragment.newInstance(sliderPage));
    }

    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putBoolean("firstStart", false);
        editor.apply();
        finish();
    }
}
