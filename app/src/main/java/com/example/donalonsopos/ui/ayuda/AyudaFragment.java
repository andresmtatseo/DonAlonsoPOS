package com.example.donalonsopos.ui.ayuda;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.donalonsopos.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AyudaFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AyudaFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AyudaFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AyudaFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AyudaFragment newInstance(String param1, String param2) {
        AyudaFragment fragment = new AyudaFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ayuda, container, false);

        WebView webView = view.findViewById(R.id.webView);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true); // Habilitar JavaScript
        webView.setWebViewClient(new WebViewClient()); // Manejar navegación dentro de la WebView

        // URL del documento en modo vista previa
        String docUrl = "https://docs.google.com/document/d/1ldjwx4D4GwTQGmyxbVkBVt-8cUPabut1K5W0whqN12A/preview";
        webView.loadUrl(docUrl);

        return view;
    }

}