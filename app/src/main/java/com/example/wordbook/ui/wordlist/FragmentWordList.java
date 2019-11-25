package com.example.wordbook.ui.wordlist;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.wordbook.R;
import com.example.wordbook.model.WordLibrary;
import com.example.wordbook.ui.MainActivity;
import com.example.wordbook.ui.share.LogTag;
import com.example.wordbook.ui.wordAdd.FragmentWordAdd;

public class FragmentWordList extends Fragment {

    private class ButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.button_addWord:
                    Log.d(LogTag.wordList, "creating fragment wordAdd.");
                    FragmentWordAdd wordAdd = new FragmentWordAdd();
                    MainActivity mainActivity = (MainActivity)getActivity();

                    if (mainActivity != null) {
                        Log.d(LogTag.wordList, "replacing fragment to wordAdd.");
                        mainActivity.replaceFragment(wordAdd);
                    } else
                        Log.e(LogTag.wordList,
                                "cannot get mainActivity! didn't start wordAdd.");
                    break;
                case R.id.button_searchWord:
                    String searchContent = textViewSearchWord.getText().toString();
                    setRecycleView(searchContent);
                    break;
            }
        }
    }

    private Button buttonAddWord, buttonSearchWord;
    private TextView textViewSearchWord;
    private RecyclerView recyclerViewWordList;
    private View container;

    private void assignView() {
        this.buttonAddWord = this.container.findViewById(R.id.button_addWord);
        this.buttonSearchWord= this.container.findViewById(R.id.button_searchWord);
        this.textViewSearchWord = this.container.findViewById(R.id.textView_searchWord);
        this.recyclerViewWordList = this.container.findViewById(R.id.recyclerView_wordlist);

        this.recyclerViewWordList.setLayoutManager(new LinearLayoutManager(this.getContext()));
    }

    private void assignClickEvent() {
        ButtonClickListener listener = new ButtonClickListener();
        buttonAddWord.setOnClickListener(listener);
        buttonSearchWord.setOnClickListener(listener);
    }

    private void setRecycleView(String word) {
        if (word.equals(""))
            word = null;
        WordListAdapter adapter = new WordListAdapter(this,
                WordLibrary.getInstance(this.container.getContext()).searchWord(word));
        this.recyclerViewWordList.setAdapter(adapter);

        Log.d(LogTag.wordDetail,
                String.format("setting recycleView, there are %s item(s)", adapter.getItemCount()));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.container = inflater.inflate(R.layout.fragment_wordlist, container, false);
        this.assignView();
        this.setRecycleView("");
        this.assignClickEvent();

        return this.container;
    }

}
