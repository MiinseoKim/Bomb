// Generated code from Butter Knife. Do not modify!
package org.androidtown.myapplication123;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class FrgCalendar$$ViewBinder<T extends org.androidtown.myapplication123.FrgCalendar> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131624127, "field 'calendarView'");
    target.calendarView = finder.castView(view, 2131624127, "field 'calendarView'");
  }

  @Override public void unbind(T target) {
    target.calendarView = null;
  }
}
