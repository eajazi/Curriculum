package reachingimmortality.com.curriculum.material_navigation_drawer.elements.listeners;


import reachingimmortality.com.curriculum.material_navigation_drawer.elements.MaterialAccount;

/**
 * Created by neokree on 11/12/14.
 */
public interface MaterialAccountListener {

    public void onAccountOpening(MaterialAccount account);

    public void onChangeAccount(MaterialAccount newAccount);

}
