package com.missionse.httpdatabaseexample;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.missionse.httpdatabaseexample.model.Classroom;

public class ClassroomListFragment extends Fragment {

	private ArrayAdapter<Classroom> listAdapter;
	private List<Classroom> classrooms = new ArrayList<Classroom>();

	@Override
	public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
		View contentView = inflater.inflate(R.layout.fragment_classroom_list, container, false);

		ListView classroomList = (ListView) contentView.findViewById(R.id.classroom_list);

		listAdapter = new ArrayAdapter<Classroom>(getActivity(), R.layout.classroom_list_entry, classrooms);
		classroomList.setAdapter(listAdapter);

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

//		listAdapter.addAll(Model.fetchAll(Classroom.class));

		return contentView;
	}

	private void createNewClassroom() {
		Random random = new Random();
		Classroom classroom = new Classroom();
		classroom.classRoomName = "Classroom#" + random.nextInt(1000);
//		classroom.insert();

		classrooms.clear();
//		classrooms.addAll(Model.fetchAll(Classroom.class));
		listAdapter.notifyDataSetChanged();
	}

	private void clearAllClassrooms() {
//		for (Classroom classroom : Model.fetchAll(Classroom.class)) {
//			classroom.delete();
//		}

		classrooms.clear();
		listAdapter.notifyDataSetChanged();
	}
}
