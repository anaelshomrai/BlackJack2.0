
package Inteface;

/**
 * This is an interface that all of the admins must implement to be an Admin.
 * All the admins have a change permission ability. they can promote a regular
 * user to be an admin, but cant demote an admin.
 * 
 * @author ANI
 */
public interface IAdmin extends IUserAction{
    
    /**
     * This method will be implemented so that an admin can change a regular
     * user permission. An admin can't demote an admin.
     * 
     * @param userId the id of the user
     */
    public void changePermission(int userId);
    
}
