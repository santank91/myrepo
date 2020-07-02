
Roles directory - Custom roles - developer and deployer are defined. New custom roles can be added as per requirement.
ClusterRolebindings directory - Have YAML files to do ClusterRolebinding i.e. binding the user to have clusterlevel access
Rolebindings directory - Have YAML files to do Rolebinding i.e. binding the user to have namespace level access

Note:- Namespace and username needs to be modified as per requirement. Templates are also defined in the directories to make changes accordingly.

Apart from the custom roles defined in Roles directory, we can also use dafault roles and bind it to the user. 
PFB the commands to do it manually-

cluster-admin role to give admin access for the user across the cluster -
----------------------------------------------------------------------------
    kubectl create clusterrolebinding adminpoc-binding --clusterrole=cluster-admin --user=adminpoc

admin role to give admin access for the user only on a particular namespace -
-------------------------------------------------------------------------------
    kubectl create rolebinding adminuser-binding --clusterrole=admin --user=adminuser --namespace=dev

edit role to give edit access for the user only on a particular namespace -
----------------------------------------------------------------------------
    kubectl create rolebinding edituser-binding --clusterrole=edit --user=edituser --namespace=dev

view role to give read-only access to user only on a particular namespace -
----------------------------------------------------------------------------
    kubectl create rolebinding readonlyuser-binding --clusterrole=view --user=readonlyuser --namespace=prod

view role to give read-only access to user across the cluster -
----------------------------------------------------------------
    kubectl create clusterrolebinding readuser-binding --clusterrole=view --user=readuser
