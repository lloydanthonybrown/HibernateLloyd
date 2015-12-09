package Sandbox.Alberto.Arellano;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class HibernateRunner {
    private List<User> users;
    private HibernateConfig theHibernateUtility;

    public HibernateRunner(){
        theHibernateUtility = new HibernateConfig();
    }

    public static void main(String[] args){

        // Instance of hibernate
        HibernateRunner aSillyHibernateUseExample = new HibernateRunner();
        // Add NEW users to database
        // aSillyHibernateUseExample.addNewUsers();
        // Show users information
        // aSillyHibernateUseExample.showAllUsers();
        // Update user information
        aSillyHibernateUseExample.modifyUser()


        }


        /*
         * show how to modify a database record
         */
        private void modifyUser() {

            Session session = theHibernateUtility.getCurrentSession();
            Transaction transaction = session.beginTransaction();
            User noamUser;
            /*
             * get a single User instance from the database.
             */
            try{
                // If name does not match it throws
                // ERROR: java.lang.NullPointerException
                Query singleUserQuery = session.createQuery("select u from User as u where u.uname='Noam Chomsky'");
                // Cast all the information into a User Variable
                noamUser = (User)singleUserQuery.uniqueResult();
            /*
             * change the user name for the Java instance
             */
                // Place the new Name
                noamUser.setUname("Michel foucault");
            /*
             * call the session merge method for the User instance in question.  This tells the database that the instance is ready to be permanently stored.
             */
                // Right now merge and save have done the same, I thought merge was going to substitute the row I created and
                // save create another one but apperantly it does the same
                // Now if you have both this is what it does->Nothing

                // I researched and this is what it does
                // save Persists an entity. Will assign an identifier if one doesn't exist. If one does, it's essentially doing an update. Returns the generated ID of the entity.

                // Merge method: it will act like update but here if a persistent object with the same identifier is already in the session it will update the detached object values
                // in the persistent object and save it.
                //
                // But if there is no persistent instance currently associated with the session, this will load the persistent object from
                // the datastore and then update the value of detached object in loaded persistent object and then update it.
                session.merge(noamUser);
    //            session.save(noamUser);
            }
            catch (Exception e)
            {
                // ERROR:
                System.out.println("User was not found");
                e.printStackTrace();
            }

            /*
             * call the transaction commit method.  This tells the database that the changes are ready to be permanently stored.
             */
            transaction.commit();
            /*
             * permanently store the changes into the database tables.
             */
            showAllUsers();
        }


}
