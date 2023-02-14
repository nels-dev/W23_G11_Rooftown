package csis3175.w23.g11.rooftown;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import csis3175.w23.g11.rooftown.messages.AllChatsFragment;

public class HomeFragment extends Fragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        LinearLayout linearLayoutHome = view.findViewById(R.id.linearLayoutHome);

        View button1 = inflater.inflate(R.layout.layout_homebutton, container, false);
        TextView buttonTitle1 = button1.findViewById(R.id.txtViewHomeButtonTitle);
        buttonTitle1.setText(R.string.txtHomeButton1Title);
        TextView buttonContent1 = button1.findViewById(R.id.txtViewHomeButtonContent);
        buttonContent1.setText(R.string.txtHomeButton1Content);
        buttonContent1.setCompoundDrawablesWithIntrinsicBounds(null,
                ResourcesCompat.getDrawable(getResources(), R.drawable.ic_outline_search_60, null ),
                null,
                null);
        CardView card1 = button1.findViewById(R.id.cardViewHomeButton);
        card1.setOnClickListener((View v) -> {
            Fragment roommatesFragment = new RoommatesFragment();
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.mainContainer, roommatesFragment)
                    .commit();
        });
        linearLayoutHome.addView(button1);

        View button2 = inflater.inflate(R.layout.layout_homebutton, container, false);
        TextView buttonTitle2 = button2.findViewById(R.id.txtViewHomeButtonTitle);
        buttonTitle2.setText(R.string.txtHomeButton2Title);
        TextView buttonContent2 = button2.findViewById(R.id.txtViewHomeButtonContent);
        buttonContent2.setText(R.string.txtHomeButton2Content);
        buttonContent2.setCompoundDrawablesWithIntrinsicBounds(null,
                ResourcesCompat.getDrawable(getResources(), R.drawable.ic_outline_add_box_60, null ),
                null,
                null);
        CardView card2 = button2.findViewById(R.id.cardViewHomeButton);
        card2.setOnClickListener((View v) -> {
            Fragment postingFragment = new PostingFragment();
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.mainContainer, postingFragment)
                    .commit();
        });
        linearLayoutHome.addView(button2);

        View button3 = inflater.inflate(R.layout.layout_homebutton, container, false);
        TextView buttonTitle3 = button3.findViewById(R.id.txtViewHomeButtonTitle);
        buttonTitle3.setText(R.string.txtHomeButton3Title);
        TextView buttonContent3 = button3.findViewById(R.id.txtViewHomeButtonContent);
        buttonContent3.setText(R.string.txtHomeButton3Content);
        buttonContent3.setCompoundDrawablesWithIntrinsicBounds(null,
                ResourcesCompat.getDrawable(getResources(), R.drawable.ic_outline_question_answer_60, null ),
                null,
                null);
        CardView card3 = button3.findViewById(R.id.cardViewHomeButton);
        card3.setOnClickListener((View v) -> {
            Fragment allChatsFragment = new AllChatsFragment();
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.mainContainer, allChatsFragment)
                    .commit();
        });
        linearLayoutHome.addView(button3);

        return view;
    }
}