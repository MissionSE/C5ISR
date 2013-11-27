package com.missionse.databaseexample;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.orman.mapper.Model;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.missionse.databaseexample.Model.Classroom;

/**
 * Displays a list of mClassrooms, and buttons to manipulate that list.
 */
public class ClassroomListFragment extends Fragment {

	private static final int MAX_CLASSROOMS = 100;

	private ArrayAdapter<Classroom> mListAdapter;
	private final List<Classroom> mClassrooms = new ArrayList<Classroom>();

	@Override
	public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
		View contentView = inflater.inflate(R.layout.fragment_classroom_list, container, false);

		ListView classroomList = (ListView) contentView.findViewById(R.id.classroom_list);

		mListAdapter = new ArrayAdapter<Classroom>(getActivity(), R.layout.classroom_list_entry, mClassrooms);
		classroomList.setAdapter(mListAdapter);

		Button addClassroomButton = (Button) contentView.findViewById(R.id.button_newclassroom);
		addClassroomButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(final View view) {
				createNewClassroom();
			}
		});

		Button clearButton = (Button) contentView.findViewById(R.id.clear_button);
		clearButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(final View view) {
				clearAllClassrooms();
			}
		});

		mListAdapter.addAll(Model.fetchAll(Classroom.class));

		return contentView;
	}

	private void createNewClassroom() {
		Random random = new Random();
		Classroom classroom = new Classroom();
		classroom.mClassRoomName = "Classroom#" + random.nextInt(MAX_CLASSROOMS);
		classroom.insert();

		mClassrooms.clear();
		mClassrooms.addAll(Model.fetchAll(Classroom.class));
		mListAdapter.notifyDataSetChanged();
	}

	private void clearAllClassrooms() {
		for (Classroom classroom : Model.fetchAll(Classroom.class)) {
			classroom.delete();
		}

		mClassrooms.clear();
		mListAdapter.notifyDataSetChanged();
	}
}
