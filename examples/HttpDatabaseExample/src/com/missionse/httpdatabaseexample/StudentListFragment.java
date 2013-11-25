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

import com.missionse.httpdatabaseexample.model.Student;
import com.missionse.httpdatabaseexample.tasks.CreateStudentTask;
import com.missionse.httpdatabaseexample.tasks.DeleteAllStudentsTask;
import com.missionse.httpdatabaseexample.tasks.GetAllStudentsTask;

public class StudentListFragment extends Fragment {

	private static final String[] FIRST_NAMES = new String[] { "James", "Kyle", "Mike", "Rob" };
	private static final String[] LAST_NAMES = new String[] { "Lehman", "Sant'Angelo", "Testen", "Vieras" };

	private ArrayAdapter<Student> listAdapter;
	private List<Student> students = new ArrayList<Student>();

	@Override
	public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
		View contentView = inflater.inflate(R.layout.fragment_student_list, container, false);

		ListView studentList = (ListView) contentView.findViewById(R.id.student_list);

		listAdapter = new ArrayAdapter<Student>(getActivity(), R.layout.student_list_entry, students);
		studentList.setAdapter(listAdapter);

		Button addStudentButton = (Button) contentView.findViewById(R.id.button_newstudent);
		addStudentButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(final View view) {
				createNewStudent();
			}
		});

		Button clearButton = (Button) contentView.findViewById(R.id.clear_button);
		clearButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(final View view) {
				clearAllStudents();
			}
		});

		getAllStudents();

		return contentView;
	}

	private void createNewStudent() {
		Random random = new Random();
		String firstName = FIRST_NAMES[random.nextInt(FIRST_NAMES.length)];
		String lastName = LAST_NAMES[random.nextInt(LAST_NAMES.length)];

		new CreateStudentTask(getActivity(), listAdapter, students).execute(firstName, lastName);
	}

	private void clearAllStudents() {
		new DeleteAllStudentsTask(getActivity(), listAdapter, students).execute();
	}

	private void getAllStudents() {
		new GetAllStudentsTask(getActivity(), listAdapter, students).execute();
	}
}
