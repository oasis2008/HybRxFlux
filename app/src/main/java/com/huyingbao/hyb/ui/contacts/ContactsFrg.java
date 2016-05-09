package com.huyingbao.hyb.ui.contacts;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.hardsoftstudio.rxflux.action.RxError;
import com.hardsoftstudio.rxflux.dispatcher.RxViewDispatch;
import com.hardsoftstudio.rxflux.store.RxStore;
import com.hardsoftstudio.rxflux.store.RxStoreChange;
import com.huyingbao.hyb.R;
import com.huyingbao.hyb.SampleApp;
import com.huyingbao.hyb.adapter.RepoAdapter;
import com.huyingbao.hyb.base.BaseFragment;
import com.huyingbao.hyb.model.GitHubRepo;
import com.huyingbao.hyb.stores.RepositoriesStore;
import com.huyingbao.hyb.stores.UsersStore;
import com.huyingbao.hyb.ui.home.UserFragment;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/5/6.
 */
public class ContactsFrg extends BaseFragment implements RxViewDispatch, RepoAdapter.OnRepoClicked {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";
    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;
    @Bind(R.id.progress_loading)
    ProgressBar progressLoading;
    @Bind(R.id.root)
    RelativeLayout root;
    @Bind(R.id.root_coordinator)
    CoordinatorLayout rootCoordinator;


    private RepoAdapter adapter;
    private UsersStore usersStore;
    private RepositoriesStore repositoriesStore;

    public ContactsFrg() {
//        repositoriesStore = RepositoriesStore.get(SampleApp.getInstance().getRxFlux().getDispatcher());
//        usersStore = UsersStore.get(SampleApp.getInstance().getRxFlux().getDispatcher());
//        repositoriesStore.register();
//        usersStore.register();
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static ContactsFrg newInstance(int sectionNumber) {
        ContactsFrg fragment = new ContactsFrg();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.f_home, container, false);
        ButterKnife.bind(this, rootView);


        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        return rootView;
    }


    @Override
    public void onResume() {
        super.onResume();
//        refresh();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.recycler_view, R.id.progress_loading, R.id.root, R.id.root_coordinator})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.recycler_view:
                break;
            case R.id.progress_loading:
                break;
            case R.id.root:
                break;
            case R.id.root_coordinator:
                break;
        }
    }

    @Override
    public void onClicked(GitHubRepo repo) {
        String login = repo.getOwner().getLogin();
        if (usersStore.getUser(login) != null) {
            showUserFragment(login);
            return;
        }
        setLoadingFrame(true);
        SampleApp.get(getContext()).getGitHubActionCreator().getUserDetails(login);
    }


    @Override
    public void onRxStoreChanged(@NonNull RxStoreChange change) {
//        setLoadingFrame(false);
//
//        switch (change.getStoreId()) {
//            case RepositoriesStore.ID:
//                switch (change.getRxAction().getType()) {
//                    case Actions.GET_PUBLIC_REPOS:
//                        if (repositoriesStore != null) {
//                            adapter.setRepos(repositoriesStore.getRepositories());
//                        }
//                        break;
//                }
//                break;
//            case UsersStore.ID:
//                switch (change.getRxAction().getType()) {
//                    case Actions.GET_USER:
//                        showUserFragment((String) change.getRxAction().getData().get(Keys.ID));
//                }
//        }
    }

    @Override
    public void onRxError(@NonNull RxError error) {
        setLoadingFrame(false);
        Throwable throwable = error.getThrowable();
        if (throwable != null) {
            Snackbar.make(rootCoordinator, "An error ocurred", Snackbar.LENGTH_INDEFINITE).setAction("Retry", v -> SampleApp.get(getContext()).getGitHubActionCreator().retry(error.getAction())).show();
            throwable.printStackTrace();
        } else {
            Toast.makeText(getContext(), "Unknown error", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onRxViewRegistered() {
        // If there is any fragment that needs to register store changes we can do it here
    }

    @Override
    public void onRxViewUnRegistered() {
        // If there is any fragment that has registered for store changes we can unregister now
    }

    @Nullable
    @Override
    public List<RxStore> getRxStoreListToRegister() {
        return null;
    }

    @Nullable
    @Override
    public List<RxStore> getRxStoreListToUnRegister() {
        return null;
    }

    private void showUserFragment(String id) {

        UserFragment user_fragment = UserFragment.newInstance(id);
        getFragmentManager().beginTransaction().replace(R.id.root, user_fragment).addToBackStack(null).commit();
    }

    private void setLoadingFrame(boolean show) {
//        progressLoading.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    private void refresh() {
        setLoadingFrame(true);
        SampleApp.get(getContext()).getGitHubActionCreator().getPublicRepositories();
    }
}