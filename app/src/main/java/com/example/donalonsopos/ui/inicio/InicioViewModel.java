package com.example.donalonsopos.ui.inicio;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class InicioViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public InicioViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("¡Bienvenido/a al Sistema de Gestión!");
    }

    public LiveData<String> getText() {
        return mText;
    }
}