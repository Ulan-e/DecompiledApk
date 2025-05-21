package kz.sapasoft.emark.app.di.builder;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import kotlin.Metadata;
import kz.sapasoft.emark.app.ui.about.AboutFragment;
import kz.sapasoft.emark.app.ui.main_fragments.MainFragment;
import kz.sapasoft.emark.app.ui.map.MapFragment;
import kz.sapasoft.emark.app.ui.marker.MarkerFragment;
import kz.sapasoft.emark.app.ui.photo.PhotoViewFragment;
import kz.sapasoft.emark.app.ui.projects.ProjectsFragment;
import kz.sapasoft.emark.app.ui.settings.SettingsFragment;

@Metadata(bv = {1, 0, 3}, d1 = {"\u00000\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b'\u0018\u00002\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0002J\b\u0010\u0003\u001a\u00020\u0004H'J\b\u0010\u0005\u001a\u00020\u0006H'J\b\u0010\u0007\u001a\u00020\bH'J\b\u0010\t\u001a\u00020\nH'J\b\u0010\u000b\u001a\u00020\fH'J\b\u0010\r\u001a\u00020\u000eH'¨\u0006\u000f"}, d2 = {"Lkz/sapasoft/emark/app/di/builder/MainActivityProviders;", "", "()V", "provideAboutFragment", "Lkz/sapasoft/emark/app/ui/about/AboutFragment;", "provideHomeDetailsFragment", "Lkz/sapasoft/emark/app/ui/map/MapFragment;", "provideHomeFragment", "Lkz/sapasoft/emark/app/ui/projects/ProjectsFragment;", "provideMainFragment", "Lkz/sapasoft/emark/app/ui/main_fragments/MainFragment;", "provideMarkerFragment", "Lkz/sapasoft/emark/app/ui/marker/MarkerFragment;", "provideSettingsFragment", "Lkz/sapasoft/emark/app/ui/settings/SettingsFragment;", "app_release"}, k = 1, mv = {1, 1, 16})
@Module
/* compiled from: MainActivityProviders.kt */
public abstract class MainActivityProviders {
    @ContributesAndroidInjector
    public abstract AboutFragment provideAboutFragment();

    @ContributesAndroidInjector
    public abstract MapFragment provideHomeDetailsFragment();

    @ContributesAndroidInjector
    public abstract ProjectsFragment provideHomeFragment();

    @ContributesAndroidInjector
    public abstract MainFragment provideMainFragment();

    @ContributesAndroidInjector
    public abstract MarkerFragment provideMarkerFragment();

    @ContributesAndroidInjector
    public abstract SettingsFragment provideSettingsFragment();

    @ContributesAndroidInjector
    public abstract PhotoViewFragment providePhotoViewFragment();
}
