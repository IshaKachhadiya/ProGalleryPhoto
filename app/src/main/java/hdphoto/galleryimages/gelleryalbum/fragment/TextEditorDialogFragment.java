package hdphoto.galleryimages.gelleryalbum.fragment;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import hdphoto.galleryimages.gelleryalbum.R;
import hdphoto.galleryimages.gelleryalbum.adapter.EdColorPickerAdapter;


public class TextEditorDialogFragment extends DialogFragment {
    public static final String GEXTRA_COLOR_CODE = "extra_color_code";
    public static final String GEXTRA_INPUT_TEXT = "extra_input_text";
    public static final String TAG = "EMTextEditorDialogFragment";
    TextView gAddTextDoneTextView;
    EditText gAddTextEditText;
    int gColorCode;
    InputMethodManager gInputMethodManager;
    TextEditor gTextEditor;

    public interface TextEditor {
        void onDone(String str, int i);
    }

    public static TextEditorDialogFragment show(AppCompatActivity appCompatActivity, String str, int i) {
        Bundle bundle = new Bundle();
        bundle.putString(GEXTRA_INPUT_TEXT, str);
        bundle.putInt(GEXTRA_COLOR_CODE, i);
        TextEditorDialogFragment eMTextEditorDialogFragment = new TextEditorDialogFragment();
        eMTextEditorDialogFragment.setArguments(bundle);
        eMTextEditorDialogFragment.show(appCompatActivity.getSupportFragmentManager(), TAG);
        return eMTextEditorDialogFragment;
    }

    public static TextEditorDialogFragment show(AppCompatActivity appCompatActivity) {
        return show(appCompatActivity, "", ContextCompat.getColor(appCompatActivity, R.color.txt_color));
    }

    @Override 
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.getWindow().setLayout(-1, -1);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        return layoutInflater.inflate(R.layout.dg_text_editor, viewGroup, false);
    }

    @Override
    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        gAddTextEditText = (EditText) view.findViewById(R.id.add_text_edit_text);
        gInputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        gAddTextDoneTextView = (TextView) view.findViewById(R.id.add_text_done_tv);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.add_text_color_picker_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false));
        recyclerView.setHasFixedSize(true);
        EdColorPickerAdapter eMColorPickerAdapter = new EdColorPickerAdapter(getActivity());
        eMColorPickerAdapter.setOnColorPickerClickListener(i -> {
            gColorCode = i;
            gAddTextEditText.setTextColor(i);
        });
        recyclerView.setAdapter(eMColorPickerAdapter);
        gAddTextEditText.setText(getArguments().getString(GEXTRA_INPUT_TEXT));
        int i = getArguments().getInt(GEXTRA_COLOR_CODE);
        gColorCode = i;
        gAddTextEditText.setTextColor(i);
        gAddTextDoneTextView.setOnClickListener(view2 -> {
            gInputMethodManager.hideSoftInputFromWindow(view2.getWindowToken(), 0);
            dismiss();
            String obj = gAddTextEditText.getText().toString();
            if (TextUtils.isEmpty(obj) || gTextEditor == null) {
                return;
            }
            gTextEditor.onDone(obj, gColorCode);
        });
    }

    public void setOnTextEditorListener(TextEditor textEditor) {
        gTextEditor = textEditor;
    }
}
