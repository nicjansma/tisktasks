package com.nicjansma.tisktasks.activities;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RelativeLayout;

import org.joda.time.DateTime;

import com.nicjansma.library.android.AndroidUtils;
import com.nicjansma.tisktasks.R;
import com.nicjansma.tisktasks.ServiceLocator;
import com.nicjansma.tisktasks.models.TodoistObjectBase;

/**
 * Task add or edit activity base.
 *
 * @param <T> Base object type
 */
public abstract class TaskAddEditActivityBase<T extends TodoistObjectBase>
    extends PopupDialogBase<T>
{
    //
    // UI
    //
    /**
     * Set Date checkbox.
     */
    private CheckBox _setDateCheckBox;

    /**
     * Date picker.
     */
    private DatePicker _datePicker;

    /**
     * Date edit text.
     */
    private EditText _dateText;

    /**
     * Date layout.
     */
    private RelativeLayout _dateOptionsLayout;

    /**
     * Use date text radio button.
     */
    private RadioButton _radioUseDateText;

    /**
     * Use date picker radio button.
     */
    private RadioButton _radioUseDatePicker;

    //
    // Extension methods
    //
    /**
     * Implemented by subclasses.
     *
     * Called at the beginning of this class' onCreate.
     *
     * @param savedInstance Saved bundle
     */
    protected abstract void onCreateInternalTaskAddEditPre(final Bundle savedInstance);

    /**
     * Implemented by subclasses.
     *
     * Called at the end of this class' onCreate.
     *
     * @param savedInstance Saved bundle
     */
    protected abstract void onCreateInternalTaskAddEditPost(final Bundle savedInstance);

    @Override
    public final void onCreateInternalPre(final Bundle savedInstanceState)
    {
        onCreateInternalTaskAddEditPre(savedInstanceState);
    }

    @Override
    public final void onCreateInternalPost(final Bundle savedInstanceState)
    {
        //
        // Inflation
        //
        _setDateCheckBox = (CheckBox) findViewById(R.id.task_add_use_date);
        _setDateCheckBox.setOnCheckedChangeListener(_setDateCheckBoxChange);

        // date/time picker
        DateTime now = new DateTime();
        _datePicker = (DatePicker) findViewById(R.id.task_add_date_picker);
        int year = now.getYear();
        int month = now.getMonthOfYear() - 1; // 0-based
        int day = now.getDayOfMonth();
        _datePicker.init(year, month, day, _datePickerOnDateChangedListener);

        _dateText = (EditText) findViewById(R.id.task_add_date_text);
        _dateText.addTextChangedListener(_dateTextTextChangedListener);

        _dateOptionsLayout = (RelativeLayout) findViewById(R.id.task_add_date_options);

        _radioUseDateText = (RadioButton) findViewById(R.id.task_add_use_date_text);
        _radioUseDatePicker = (RadioButton) findViewById(R.id.task_add_use_date_picker);

        _radioUseDateText.setOnClickListener(_radioUseChange);
        _radioUseDatePicker.setOnClickListener(_radioUseChange);

        onCreateInternalTaskAddEditPost(savedInstanceState);
    }

    /**
     * Date Text Text Changed Listener.
     */
    private final TextWatcher _dateTextTextChangedListener = new TextWatcher() {
        @Override
        public void afterTextChanged(final Editable s)
        {
            useDateText();
        }

        @Override
        public void beforeTextChanged(final CharSequence s, final int start, final int count, final int after)
        {
            // nop
        }

        @Override
        public void onTextChanged(final CharSequence s, final int start, final int before, final int count)
        {
            // nop
        }
    };

    /**
     * Date Picker on-changed listener.
     */
    private final OnDateChangedListener _datePickerOnDateChangedListener = new OnDateChangedListener() {
        @Override
        public void onDateChanged(final DatePicker view, final int year, final int monthOfYear, final int dayOfMonth)
        {
            hideKeyboard();

            useDatePicker();
        }
    };

    /**
     * Use the Date Picker.
     */
    protected final void useDatePicker()
    {
        _radioUseDatePicker.setChecked(true);
        _radioUseDateText.setChecked(false);
    }

    /**
     * Use the Date Text field.
     */
    protected final void useDateText()
    {
        _radioUseDateText.setChecked(true);
        _radioUseDatePicker.setChecked(false);
    }

    /**
     * Radio button change listener.
     */
    private final CompoundButton.OnClickListener _radioUseChange = new CompoundButton.OnClickListener() {
        @Override
        public void onClick(final View v)
        {
            hideKeyboard();

            // emulate radio group
            if (v.getId() == R.id.task_add_use_date_text)
            {
                _radioUseDatePicker.setChecked(!_radioUseDateText.isChecked());
            }
            else
            {
                _radioUseDateText.setChecked(!_radioUseDatePicker.isChecked());
            }
        }
    };

    /**
     * Set date check-box change.
     */
    private final CompoundButton.OnCheckedChangeListener _setDateCheckBoxChange =
        new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(final CompoundButton buttonView, final boolean isChecked)
        {
            _dateOptionsLayout.setVisibility(isChecked ? View.VISIBLE : View.GONE);
        }
    };

    /**
     * Determines if the date is checked or not.
     *
     * @return True if the date is checked
     */
    protected final Boolean dateIsChecked()
    {
        return _setDateCheckBox.isChecked();
    }

    /**
     * Gets the selected date from the date text box or the date picker, depending on the
     * radio button.
     *
     * @return Selected date
     */
    protected final String getSelectedDate()
    {
        String date = null;

        if (_radioUseDateText.isChecked())
        {
            date = _dateText.getText().toString();

            // Todoist API chokes if EV is in uppercase
            if (date.startsWith("EV "))
            {
                date = date.replaceFirst("EV ", "ev ");
            }

            if (date.length() == 0)
            {
                // don't send an empty date
                date = null;
            }
        }
        else
        {
            // month is 0-based
            date = ServiceLocator.userManager().getCurrentUser().getFormattedDate(_datePicker.getYear(),
                                                                           _datePicker.getMonth() + 1,
                                                                           _datePicker.getDayOfMonth());
        }

        return date;
    }

    /**
     * Gets the Date Edit Text.
     *
     * @return Date Edit Text.
     */
    protected final EditText getDateEditText()
    {
        return _dateText;
    }

    /**
     * Gets the Date Picker.
     *
     * @return Date Picker
     */
    protected final DatePicker getDatePicker()
    {
        return _datePicker;
    }

    /**
     * Gets the Date Check Box.
     *
     * @return Date Check Box
     */
    protected final CheckBox getSetDateCheckBox()
    {
        return _setDateCheckBox;
    }

    /**
     * Hides the on-screen keyboard.
     */
    private void hideKeyboard()
    {
        AndroidUtils.hideSoftKeyboard(_radioUseDatePicker.getContext(), _radioUseDatePicker.getWindowToken());
    }
}
