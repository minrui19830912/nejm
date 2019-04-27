package com.android.nejm.Fragments;

import android.support.v4.app.Fragment;

public class BaseFragment extends Fragment {
    protected Context mContext;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext= getActivity();
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        LoadingDialog.cancelDialogForLoading();
    }
}
