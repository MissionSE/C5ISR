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
import com.missionse.httpdatabaseexample.tasks.CreateClassroomTask;
import com.missionse.httpdatabaseexample.tasks.DeleteAllClassroomsTask;
import com.missionse.httpdatabaseexample.tasks.GetAllClassroomsTask;

/**
 * A fragment that contains a list of classrooms provided by a database.
 */
public class ClassroomListFragment extends Fragment {
	private static final int MAX_CLASSROOM_NUMBER = 1000;
	private ArrayAdapter<Classroom> mListAdapter;
	private List<Classroom> mClassrooms = new ArrayList<Classroom>();

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

		getAllClassrooms();

		return contentView;
	}

	private void createNewClassroom() {
		Random random = new Random();
		String classroomName = "Classroom#" + random.nextInt(MAX_CLASSROOM_NUMBER);
		new CreateClassroomTask(getActivity(), mListAdapter, mClassrooms).execute(classroomName);
	}

	private void clearAllClassrooms() {
		new DeleteAllClassroomsTask(getActivity(), mListAdapter, mClassrooms).execute();
	}

	private void getAllClassrooms() {
		new GetAllClassroomsTask(getActivity(), mListAdapter, mClassrooms).execute();
	}
}
