package com.roboboost;

import android.app.Activity;
import android.widget.Toast;
import com.pivotallabs.injected.InjectedTestRunner;
import com.xtremelabs.robolectric.Robolectric;
import com.xtremelabs.robolectric.shadows.ShadowToast;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

@RunWith(InjectedTestRunner.class)
public class ToasterTest {
    @Test
    public void showToast_shouldShowAToastWithTheProvidedMessage() throws Exception {
        Toaster.showToast(new Activity(), "Hello");
        assertThat(ShadowToast.shownToastCount(), equalTo(1));
        assertThat(ShadowToast.getTextOfLatestToast(), equalTo("Hello"));
    }

    @Test
    public void showToast_shouldShowTheToastOnTheUIThread() throws Exception {
        Robolectric.pauseMainLooper();

        Toaster.showToast(new Activity(), "Hello");
        assertThat(ShadowToast.shownToastCount(), equalTo(0));

        Robolectric.unPauseMainLooper();
        assertThat(ShadowToast.shownToastCount(), equalTo(1));
    }

    // TODO fix in robolectric - doesn't shadow toast duration correctly
    // @Test
    public void showToast_shouldHaveADefaultDuration() throws Exception {
        Toaster.showToast(new Activity(), "Hello");
        assertThat(ShadowToast.getLatestToast().getDuration(), equalTo(Toast.LENGTH_SHORT));
    }

    // TODO fix in robolectric - doesn't shadow toast duration correctly
    // @Test
    public void showToast_shouldLetYouOverrideTheDuration() throws Exception {
        Toaster.showToast(new Activity(), "Hello", Toast.LENGTH_LONG);
        assertThat(ShadowToast.getLatestToast().getDuration(), equalTo(Toast.LENGTH_LONG));
    }

}
