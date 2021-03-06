package com.com.technoparkproject.view.fragments;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.util.ArrayMap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.com.technoparkproject.R;
import com.com.technoparkproject.TestErrorShower;
import com.com.technoparkproject.broadcasts.BroadcastUpdateListRecords;
import com.com.technoparkproject.interfaces.MainListRecordsInterface;
import com.com.technoparkproject.models.Record;
import com.com.technoparkproject.models.Topic;
import com.com.technoparkproject.view.activities.MainActivity;
import com.com.technoparkproject.view.adapters.main_list_records.RecyclerTopicsWithRecordsAdapter;
import com.com.technoparkproject.view_models.MainListOfRecordsViewModel;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainListOfRecordsFragment extends Fragment implements MainListRecordsInterface {
    private RecyclerTopicsWithRecordsAdapter adapter;
    private AutoCompleteTextView searchingField;
    MainListOfRecordsViewModel viewModel;

    private final BroadcastUpdateListRecords receiverUpdateList = new BroadcastUpdateListRecords();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        ((MainActivity) getActivity()).setToolbar(getString(R.string.fragment_home_name));
        ViewGroup view = (ViewGroup) LayoutInflater.from(getContext())
                .inflate(R.layout.fragment_main_list_records, container,
                        false);
        viewModel = new ViewModelProvider(getActivity()).get(MainListOfRecordsViewModel.class);
        RecyclerView rvMainList = view.findViewById(R.id.mlr_rv_main_list);
        rvMainList.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new RecyclerTopicsWithRecordsAdapter(this);
        rvMainList.setAdapter(adapter);
        searchingField = view.findViewById(R.id.mlr_et_searching);
        final ProgressBar loadProgress = view.findViewById(R.id.list_load_progress);
        loadProgress.setVisibility(View.VISIBLE);
        viewModel.getLoadStatus().observe(getViewLifecycleOwner(), loadStatus -> {
            switch (loadStatus){
                case NO_CONNECTION:
                    Toast.makeText(getContext(),
                            getString(R.string.error_no_connection),
                            Toast.LENGTH_SHORT).show();
                    break;
                case NO_DATA:
                    Toast.makeText(getContext(),
                            getString(R.string.no_available_data),
                            Toast.LENGTH_SHORT).show();
                    break;
            }
            loadProgress.setVisibility(View.GONE);
        });

        SwipeRefreshLayout swipeLayout = view.findViewById(R.id.swipe_container);
        swipeLayout.setColorSchemeResources(R.color.colorRed);
        swipeLayout.setOnRefreshListener(() -> {
            viewModel.queryRecordTopics();
            swipeLayout.setRefreshing(false);
        });
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        observeToData(viewModel);
    }

    private void observeToData(final MainListOfRecordsViewModel viewModel) {
        viewModel.getTopicRecords().observe(getViewLifecycleOwner(), topicRecs -> {
            List<String> topicNames = new ArrayList<>();
            for (Topic topic : topicRecs.keySet()) {
                topicNames.add(topic.name);
            }
            setAutoCompleteValues(topicNames);
            adapter.setItems(topicRecs);
        });

        viewModel.setSearchingInput(searchingField);
        viewModel.getSearchingValue().observe(getViewLifecycleOwner(), s -> adapter.filterItemsByTopicName(s));
    }

    private void setAutoCompleteValues(List<String> names) {
        if(getContext() == null) return;
        ArrayAdapter adapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_dropdown_item_1line, names);
        searchingField.setAdapter(adapter);
    }

    @Override
    public void showAllRecords(String topicUUID) {
        TestErrorShower.showErrorDevelopment(getContext());
    }

    @Override
    public void showRecordMoreFun(final Record record) {
        if(getContext() == null) return;

        final Dialog dialog = new BottomSheetDialog(getContext(), R.style.BottomSheetDialog);

        @SuppressLint("InflateParams")
        View bottomSheetView = getLayoutInflater()
                .inflate(R.layout.mlr_bottom_sheet_record_functions, null);
        dialog.setContentView(bottomSheetView);
        bottomSheetView.findViewById(R.id.mlr_add_to_queue)
                .setOnClickListener(v -> {
                    viewModel.addToPlaylistClicked(record);
                    dialog.dismiss();
                });
        bottomSheetView.findViewById(R.id.mlr_go_to_account)
                .setOnClickListener(v -> {
                    ((MainActivity) getActivity()).onClickGoToAccount(record.userUUID);
                    dialog.dismiss();
                });
        dialog.show();
    }

    @Override
    public void itemClicked(Record record) {
        viewModel.itemClicked(record);
    }

    @Override
    public LiveData<String> getNowPlayingRecordUUID() {
        return viewModel.nowPlayingRecordUUID;
    }


    @Override
    public void onResume() {
        super.onResume();

        requireActivity().registerReceiver(receiverUpdateList, receiverUpdateList.getIntentFilter());
        receiverUpdateList.setListener(() -> viewModel.queryRecordTopics());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            requireActivity().unregisterReceiver(receiverUpdateList);
        }
        catch (Exception ignored){}
    }
}
