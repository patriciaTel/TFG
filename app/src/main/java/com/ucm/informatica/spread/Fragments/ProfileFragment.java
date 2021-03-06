package com.ucm.informatica.spread.Fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.Fragment;
import android.support.v4.util.Pair;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.ucm.informatica.spread.Model.Colours;
import com.ucm.informatica.spread.Presenter.ProfileFragmentPresenter;
import com.ucm.informatica.spread.R;
import com.ucm.informatica.spread.Utils.TelegramRecyclerAdapter;
import com.ucm.informatica.spread.View.ProfileFragmentView;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

import static android.view.View.VISIBLE;
import static com.ucm.informatica.spread.Utils.Constants.LocalPreferences.AGE_PREF;
import static com.ucm.informatica.spread.Utils.Constants.LocalPreferences.KEY_PREF;
import static com.ucm.informatica.spread.Utils.Constants.LocalPreferences.NAME_PREF;
import static com.ucm.informatica.spread.Utils.Constants.LocalPreferences.OTHER_PREF;
import static com.ucm.informatica.spread.Utils.Constants.LocalPreferences.PANTS_PREF;
import static com.ucm.informatica.spread.Utils.Constants.LocalPreferences.PROFILE_PREF;
import static com.ucm.informatica.spread.Utils.Constants.LocalPreferences.RESPONSE_PREF;
import static com.ucm.informatica.spread.Utils.Constants.LocalPreferences.TELEGRAM_GROUPS_NUMBER_PREF;
import static com.ucm.informatica.spread.Utils.Constants.LocalPreferences.TELEGRAM_GROUP_CHAT_ID_PREF;
import static com.ucm.informatica.spread.Utils.Constants.LocalPreferences.TELEGRAM_GROUP_NAME_PREF;
import static com.ucm.informatica.spread.Utils.Constants.LocalPreferences.TSHIRT_PREF;

public class ProfileFragment extends Fragment implements ProfileFragmentView {

    private Button editProfileButton;
    private Button saveProfileButton;
    private Button editTelegramGroupButton;

    private Button[] shirtButton = new Button[Colours.values().length];
    private Button[] pantsButton = new Button[Colours.values().length];
    private Button editWatchwordButton;
    private Button saveWatchwordButton;
    private TextView nameText;
    private TextView ageText;
    private EditText editName;
    private EditText editAge;
    private TextView watchwordTitleText;
    private TextView watchwordMessageText;
    private TextView watchwordResponseText;
    private EditText editWatchwordMessage;
    private EditText editWatchwordResponse;
    private EditText editOtherInfo;

    private ImageView profileImage;
    private Colours shirtColour = Colours.NA;
    private Colours pantsColour = Colours.NA;

    private int infoClickCounter=0;

    private RecyclerView telegramRecyclerView;
    private List<Pair<String, String>> telegramGroupList;


    private View view;

    public ProfileFragment() { }

    private ProfileFragmentPresenter profileFragmentPresenter;

    private SharedPreferences sharedPreferences;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        profileFragmentPresenter = new ProfileFragmentPresenter(this);
        sharedPreferences = Objects.requireNonNull(getContext()).getSharedPreferences(PROFILE_PREF, Context.MODE_PRIVATE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_profile, container, false);
        profileFragmentPresenter.onStart();
        profileFragmentPresenter.onRefreshView(shirtColour, pantsColour);
        return view;
    }

    public void initView(){
        editProfileButton = view.findViewById(R.id.editProfileButton);
        saveProfileButton = view.findViewById(R.id.saveProfileButton);
        editTelegramGroupButton = view.findViewById(R.id.editTelegramGroupButton);
        telegramRecyclerView = view.findViewById(R.id.telegramGroupRecyclewView);

        editWatchwordButton = view.findViewById(R.id.editWatchwordButton);
        saveWatchwordButton = view.findViewById(R.id.saveWatchwordButton);
        profileImage = view.findViewById(R.id.imageProfileView);

        initShirtButtons();
        initPantsButtons();
        initProfilePhoto();

        nameText = view.findViewById(R.id.dataNameDescription);
        ageText = view.findViewById(R.id.dataAgeDescription);
        editName = view.findViewById(R.id.editName);
        editAge = view.findViewById(R.id.editAge);
        watchwordTitleText = view.findViewById(R.id.watchwordTitle);
        watchwordMessageText = view.findViewById(R.id.watchwordMessageDescription);
        watchwordResponseText = view.findViewById(R.id.watchwordResponseDescription);
        editWatchwordMessage = view.findViewById(R.id.editWatchwordMessageDescription);
        editWatchwordResponse = view.findViewById(R.id.editWatchwordResponseDescription);
        editOtherInfo = view.findViewById(R.id.otherInfoContent);
    }

    private void initTelegramGroups() {
        telegramRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        TelegramRecyclerAdapter adapter = new TelegramRecyclerAdapter(getContext(), telegramGroupList);
        telegramRecyclerView.addItemDecoration(new DividerItemDecoration(
                Objects.requireNonNull(getContext()), LinearLayoutManager.VERTICAL));
        telegramRecyclerView.setAdapter(adapter);
    }


    private void initProfilePhoto(){
        try {
            Random rand = new Random();
            int index = rand.nextInt(24);
            InputStream ims = Objects.requireNonNull(getActivity()).getAssets().open("monster/monster-"+index+".png");
            Drawable d = Drawable.createFromStream(ims, null);
            profileImage.setForeground(d);
        }
        catch(IOException e) {
            Log.e("PROFILE PICTURE", e.getMessage());
        }
    }

    private void initShirtButtons(){
        shirtButton[Colours.NA.ordinal()] = view.findViewById(R.id.upperImageNA);
        shirtButton[Colours.White.ordinal()] = view.findViewById(R.id.upperImageWhite);
        shirtButton[Colours.Gray.ordinal()] = view.findViewById(R.id.upperImageGray);
        shirtButton[Colours.Black.ordinal()] = view.findViewById(R.id.upperImageBlack);
        shirtButton[Colours.Red.ordinal()] = view.findViewById(R.id.upperImageRed);
        shirtButton[Colours.Green.ordinal()] = view.findViewById(R.id.upperImageGreen);
        shirtButton[Colours.Blue.ordinal()] = view.findViewById(R.id.upperImageBlue);
        shirtButton[Colours.Yellow.ordinal()] = view.findViewById(R.id.upperImageYellow);
    }

    private void initPantsButtons(){
        pantsButton[Colours.NA.ordinal()] = view.findViewById(R.id.downImageNA);
        pantsButton[Colours.White.ordinal()] = view.findViewById(R.id.downImageWhite);
        pantsButton[Colours.Gray.ordinal()] = view.findViewById(R.id.downImageGray);
        pantsButton[Colours.Black.ordinal()] = view.findViewById(R.id.downImageBlack);
        pantsButton[Colours.Red.ordinal()] = view.findViewById(R.id.downImageRed);
        pantsButton[Colours.Green.ordinal()] = view.findViewById(R.id.downImageGreen);
        pantsButton[Colours.Blue.ordinal()] = view.findViewById(R.id.downImageBlue);
        pantsButton[Colours.Yellow.ordinal()] = view.findViewById(R.id.downImageYellow);
    }

    public void setupListeners(){
        profileImage.setOnClickListener(view-> {

            if(infoClickCounter==10) {
                AlertDialog dialogBuilder = new AlertDialog.Builder(Objects.requireNonNull(getActivity())).create();
                View dialogView = getLayoutInflater().inflate(R.layout.dialog_extra_info, null);

                Objects.requireNonNull(dialogBuilder.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);
                Animation shake = AnimationUtils.loadAnimation(getContext(), R.anim.shakeanimation);
                dialogView.findViewById(R.id.image1).setAnimation(shake);
                dialogView.findViewById(R.id.image2).setAnimation(shake);

                dialogBuilder.setView(dialogView);
                dialogBuilder.show();

                infoClickCounter=0;
            }
            infoClickCounter++;
        });
        editProfileButton.setOnClickListener(view -> profileFragmentPresenter.onEditPressed());
        saveProfileButton.setOnClickListener(view -> profileFragmentPresenter.onSavePressed());
        editWatchwordButton.setOnClickListener(view -> profileFragmentPresenter.onEditWatchwordPressed());
        saveWatchwordButton.setOnClickListener(view -> profileFragmentPresenter.onSaveWatchwordPressed());
        editOtherInfo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {  }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                sharedPreferences.edit().putString(OTHER_PREF, s.toString()).apply();
            }
        });
        for(int i=0; i<Colours.values().length; i++){
            shirtButton[i].setOnClickListener(view -> profileFragmentPresenter.onShirtPressed());
        }
        for(int i=0; i<Colours.values().length; i++){
            pantsButton[i].setOnClickListener(view -> profileFragmentPresenter.onPantsPressed());
        }

        watchwordTitleText.setOnClickListener(v -> {
            BottomSheetDialog dialogBuilder = new BottomSheetDialog(Objects.requireNonNull(getContext()));
            LayoutInflater inflater = getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.dialog_watchword_info, null);
            dialogBuilder.setContentView(dialogView);
            dialogBuilder.show();
        });

        editTelegramGroupButton.setOnClickListener(view -> {
            final AlertDialog dialogBuilder = new AlertDialog.Builder(Objects.requireNonNull(getContext())).create();
            LayoutInflater inflater = getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.dialog_add_telegram, null);

            EditText chatNameEditText, chatIdEditText;
            TextView passwordErrorText;
            Button storeButton, cancelButton;
            chatNameEditText = dialogView.findViewById(R.id.chatNameEditText);
            chatIdEditText = dialogView.findViewById(R.id.chatIdEditText);
            storeButton = dialogView.findViewById(R.id.storeButton);
            cancelButton = dialogView.findViewById(R.id.cancelButton);
            passwordErrorText = dialogView.findViewById(R.id.passwordErrorText);

            chatIdEditText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    passwordErrorText.setVisibility(View.GONE);
                }

                @Override
                public void afterTextChanged(Editable s) { }
            });
            storeButton.setOnClickListener(v -> {
                if(!chatIdEditText.getText().toString().isEmpty()
                        && !chatNameEditText.getText().toString().isEmpty()
                        && !chatNameEditText.getText().toString().matches("-?\\d+(\\.\\d+)?")) {

                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putInt(TELEGRAM_GROUPS_NUMBER_PREF, telegramGroupList.size()+1);
                    editor.putString(TELEGRAM_GROUP_NAME_PREF + telegramGroupList.size(),
                            chatNameEditText.getText().toString());
                    editor.putString(TELEGRAM_GROUP_CHAT_ID_PREF + telegramGroupList.size(),
                            chatIdEditText.getText().toString());
                    editor.apply();

                    telegramGroupList.add(new Pair<>(chatNameEditText.getText().toString(),
                            chatIdEditText.getText().toString()));

                    telegramRecyclerView.getAdapter().notifyDataSetChanged();
                    dialogBuilder.dismiss();
                } else {
                    passwordErrorText.setVisibility(VISIBLE);
                }
            });
            cancelButton.setOnClickListener(v -> dialogBuilder.dismiss());

            dialogBuilder.setView(dialogView);
            dialogBuilder.show();
        });
    }

    @Override
    public void onRenderView(Boolean edit) {
        if (edit) {
            editProfileButton.setVisibility(View.GONE);
            saveProfileButton.setVisibility(VISIBLE);
            nameText.setVisibility(View.GONE);
            ageText.setVisibility(View.GONE);
            editName.setText(nameText.getText());
            editAge.setText(ageText.getText());
            editName.setVisibility(VISIBLE);
            editAge.setVisibility(VISIBLE);
        } else{
            editProfileButton.setVisibility(VISIBLE);
            saveProfileButton.setVisibility(View.GONE);
            if(!editName.getText().toString().isEmpty())
                nameText.setText(editName.getText());
            if(!editAge.getText().toString().isEmpty())
                ageText.setText(editAge.getText());
            nameText.setVisibility(VISIBLE);
            ageText.setVisibility(VISIBLE);
            editName.setVisibility(View.GONE);
            editAge.setVisibility(View.GONE);
        }
    }

    public void saveData(){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if(!editName.getText().toString().isEmpty())
            editor.putString(NAME_PREF, editName.getText().toString());
        if(!editAge.getText().toString().isEmpty())
            editor.putString(AGE_PREF, editAge.getText().toString());
        editor.apply();
    }

    @Override
    public void onRenderWatchwordView(Boolean edit) {
        if (edit) {
            editWatchwordButton.setVisibility(View.GONE);
            saveWatchwordButton.setVisibility(VISIBLE);
            watchwordMessageText.setVisibility(View.GONE);
            watchwordResponseText.setVisibility(View.GONE);
            editWatchwordMessage.setText(watchwordMessageText.getText());
            editWatchwordResponse.setText(watchwordResponseText.getText());
            editWatchwordMessage.setVisibility(VISIBLE);
            editWatchwordResponse.setVisibility(VISIBLE);
        } else{
            editWatchwordButton.setVisibility(VISIBLE);
            saveWatchwordButton.setVisibility(View.GONE);
            if(!editWatchwordMessage.getText().toString().isEmpty())
                watchwordMessageText.setText(editWatchwordMessage.getText());
            if(!editWatchwordResponse.getText().toString().isEmpty())
                watchwordResponseText.setText(editWatchwordResponse.getText());
            watchwordMessageText.setVisibility(VISIBLE);
            watchwordResponseText.setVisibility(VISIBLE);
            editWatchwordMessage.setVisibility(View.GONE);
            editWatchwordResponse.setVisibility(View.GONE);
        }
    }

    public void saveWatchwordData(){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if(!editWatchwordMessage.getText().toString().isEmpty())
            editor.putString(KEY_PREF, editWatchwordMessage.getText().toString());
        if(!editWatchwordResponse.getText().toString().isEmpty())
            editor.putString(RESPONSE_PREF, editWatchwordResponse.getText().toString());
        editor.apply();
    }

    public void loadData(){
        nameText.setText(sharedPreferences.getString(NAME_PREF, ""));
        ageText.setText(sharedPreferences.getString(AGE_PREF, ""));
        shirtColour = Colours.values()[sharedPreferences.getInt(TSHIRT_PREF, 0)];
        pantsColour = Colours.values()[sharedPreferences.getInt(PANTS_PREF, 0)];
        watchwordMessageText.setText(sharedPreferences.getString(KEY_PREF, ""));
        watchwordResponseText.setText(sharedPreferences.getString(RESPONSE_PREF, ""));
        editOtherInfo.setText(sharedPreferences.getString(OTHER_PREF, ""));

        telegramGroupList = new ArrayList<>();
        int numTelegramGroups = sharedPreferences.getInt(TELEGRAM_GROUPS_NUMBER_PREF, 0);
        for(int i=0; i<numTelegramGroups; i++) {
            telegramGroupList.add(new Pair<>(
                    sharedPreferences.getString(TELEGRAM_GROUP_NAME_PREF+i, "Grupo "+i),
                    sharedPreferences.getString(TELEGRAM_GROUP_CHAT_ID_PREF+i, "")));
        }
        initTelegramGroups();
    }

    public void refreshView(){
        shirtButton[0].setVisibility(View.GONE);
        shirtButton[shirtColour.ordinal()].setVisibility(VISIBLE);
        pantsButton[0].setVisibility(View.GONE);
        pantsButton[pantsColour.ordinal()].setVisibility(VISIBLE);
    }

    public void changeShirt(Colours colour){
        SharedPreferences.Editor editor = sharedPreferences.edit();

        if(colour.ordinal()-1 < 0)
            shirtButton[Colours.values().length-1].setVisibility(View.GONE);
        else
            shirtButton[colour.ordinal()-1].setVisibility(View.GONE);
        shirtButton[colour.ordinal()].setVisibility(VISIBLE);

        editor.putInt(TSHIRT_PREF, colour.ordinal());
        editor.apply();
    }

    public void changePants(Colours colour){
        SharedPreferences.Editor editor = sharedPreferences.edit();

        if(colour.ordinal()-1 < 0)
            pantsButton[Colours.values().length-1].setVisibility(View.GONE);
        else
            pantsButton[colour.ordinal()-1].setVisibility(View.GONE);
        pantsButton[colour.ordinal()].setVisibility(VISIBLE);

        editor.putInt(PANTS_PREF, colour.ordinal());
        editor.apply();
    }
}
