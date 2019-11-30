package com.example.wordbook.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.wordbook.HelpActivity;
import com.example.wordbook.R;
import com.example.wordbook.ui.share.LogTag;
import com.example.wordbook.ui.wordAdd.FragmentWordAdd;
import com.example.wordbook.ui.wordDetail.FragmentWordDetail;
import com.example.wordbook.ui.wordlist.FragmentWordList;

public class MainActivity extends AppCompatActivity {

    private enum Status {WORD_LIST, WORD_ADD, WORD_DETAIL}

    private boolean portrait;
    private FragmentWordDetail wordDetail;
    private FragmentWordAdd wordAdd;
    private FragmentWordList wordList;
    private Status status = Status.WORD_LIST;

    private void initWordListFragment() {
        Log.d(LogTag.mainActivity, "setting the wordList fragment.");

        this.wordList = new FragmentWordList();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frame_left, this.wordList);
        transaction.commit();
    }

    // replace right frameLayout if screen-lan. Or it will replace left frameLayout.
    // it will keep the back stack's size as one.
    public void replaceFragment(Fragment fragment) {
        Log.d(LogTag.mainActivity, "replacing fragment, " + fragment.getClass().getName());

        // clear the back stack, or when user press a word multi times,
        // it will push multi fragments into back stack.
        this.getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

        if (fragment instanceof FragmentWordDetail) {
            this.wordDetail = (FragmentWordDetail) fragment;
            this.status = Status.WORD_DETAIL;
        }
        if (fragment instanceof FragmentWordAdd) {
            this.wordAdd = (FragmentWordAdd) fragment;
            this.status = Status.WORD_ADD;
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(this.portrait ? R.id.frame_left : R.id.frame_right, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    // re-replace fragment, used when the content view changed.
    private void reInitWordListFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().remove(fragment).commit();
        fragmentManager.executePendingTransactions();

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frame_left, this.wordList);
        transaction.commit();
    }

    // re-replace fragment, used when the content view changed.
    private void reReplaceFragment(Fragment fragment) {
        FragmentManager manager = this.getSupportFragmentManager();
        manager.beginTransaction().remove(fragment).commit();
        manager.executePendingTransactions();

        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(this.portrait ? R.id.frame_left : R.id.frame_right, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        this.portrait = newConfig.orientation == Configuration.ORIENTATION_PORTRAIT;

        this.getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        this.setContentView(R.layout.activity_main);

        this.reInitWordListFragment(this.wordList);
        if (this.status == Status.WORD_DETAIL)
            this.reReplaceFragment(this.wordDetail);
        if (this.status == Status.WORD_ADD)
            this.reReplaceFragment(this.wordAdd);
    }

    @Override
    public void onBackPressed() {
        switch (this.status) {
            case WORD_ADD:
            case WORD_DETAIL:
                this.status = Status.WORD_LIST;
                Log.d(LogTag.mainActivity, "set status to WORD LIST");
                break;
            case WORD_LIST:
                Log.i(LogTag.mainActivity, "pressed back on word list, exiting.");
                break;
        }

        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = this.getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);
        if (item.getItemId() == R.id.menuItem_main_help) {
            Intent intent = new Intent(this, HelpActivity.class);
            this.startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.portrait = this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
        this.initWordListFragment();
    }

}
