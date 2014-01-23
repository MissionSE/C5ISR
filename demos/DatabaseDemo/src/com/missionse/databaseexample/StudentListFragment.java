package com.missionse.databaseexample;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.missionse.databaseexample.Model.Student;

/**
 * Displays a list of mStudents, and buttons for manipulating the list.
 */
public class StudentListFragment extends Fragment {

	private static final String[] FIRST_NAMES = new String[] { "James", "Kyle", "Mike", "Rob" };
	private static final String[] LAST_NAMES = new String[] { "Lehman", "Sant'Angelo", "Testen", "Vieras" };

	private ArrayAdapter<Student> mListAdapter;
	private final List<Student> mStudents = new ArrayList<Student>();
	private IDatabaseAccess mDBAccess;

	@Override
	public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
		View contentView = inflater.inflate(R.layout.fragment_student_list, container, false);

		ListView studentList = (ListView) contentView.findViewById(R.id.student_list);

		mListAdapter = new ArrayAdapter<Student>(getActivity(), R.layout.student_list_entry, mStudents);
		studentList.setAdapter(mListAdapter);

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

		mListAdapter.addAll(queryAll());

		return contentView;
	}

	private void createNewStudent() {
		Random random = new Random();
		Student student = new Student();
		student.setFirstName(FIRST_NAMES[random.nextInt(FIRST_NAMES.length)]);
		student.setLastName(LAST_NAMES[random.nextInt(FIRST_NAMES.length)]);
		
		try {
			mDBAccess.getDBHelper().getStudentDao().create(student);
		} catch (SQLException sql) {
			Log.e("StudentListFragment", "Create", sql);
		}

		mStudents.clear();
		mStudents.addAll(queryAll());
		mListAdapter.notifyDataSetChanged();
	}

	private void clearAllStudents() {
		for (Student student : queryAll()) {
			try {
			mDBAccess.getDBHelper().getStudentDao().delete(student);
			} catch (SQLException sql) {
				Log.e("StudentListFragment", "Delete", sql);
			}
		}

		mStudents.clear();
		mListAdapter.notifyDataSetChanged();
	}
	
	private List<Student> queryAll() {
		try {
			return mDBAccess.getDBHelper().getStudentDao().queryForAll();
		} catch (SQLException sql) {
			Log.e("StudentListFragment", "QueryAll", sql);
			return Collections.emptyList();
		}
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mDBAccess = (IDatabaseAccess) activity;
		} catch (ClassCastException exception) {
			Log.e("ClassRoomFragment", "Unable to attach DBAccess. ", exception);
		}
	}
	
}
