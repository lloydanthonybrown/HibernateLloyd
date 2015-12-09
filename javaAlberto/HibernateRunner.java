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
        aSillyHibernateUseExample.addNewUsers();
        // Show users information
        aSillyHibernateUseExample.showAllUsers();
        // Update user information
        aSillyHibernateUseExample.modifyUser();
        // Add a number to one of the users
        aSillyHibernateUseExample.addSharedPhoneNumber();
        //aSillyHibernateUseExample.deleteAddedUsers();

    }

    /*
    * show how to add records to the database
    */
    private void addNewUsers() {
        Session session = theHibernateUtility.getCurrentSession();
               /*
                * all database interactions in Hibernate are required to be inside a transaction.
                */
        Transaction transaction = session.beginTransaction();
               /*
                * create some User instances.
                */

               /* My EXPERIMENTATION
                */
        User noamUser = new User();
        try{
            noamUser.setUname("Noam Chomsky");
            noamUser.setPword("qwerty");
        }
        catch(Exception e)
        {
            // Throws: com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException: Column 'pword' cannot be null
            // ERROR: Column 'pword' cannot be null
            // Throws:  com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException: Column 'uname' cannot be null
            // ERROR: Column 'uname' cannot be null
            System.out.println("Necessary field missing either username or Password");
            e.printStackTrace();
        }

               /*
                * save each instance as a record in the database
                */

        // Save it to the session otherwise it won't work
        try{

            session.save(noamUser);
            // session.merge(noamUser); // !!! Cannot do!!!
        }
        catch (Exception e)
        {
            // ERROR: HHH000099: an assertion failure occured (this may indicate a bug in Hibernate, but is more likely due to unsafe use of the session):
            // org.hibernate.AssertionFailure: null id in Sandbox.Alberto.Arellano.User entry (don't flush the Session after an exception occurs)
            System.out.println("Need to save session before committing, Cannot do merge when there is no data save to the session");
            e.printStackTrace();
        }
        transaction.commit();
               /*
                * prove that the User instances were added to the database and that
                * the instances were each updated with a database generated id.
                */

        // Chomsky ID is
        System.out.println("Noam Chomsky's generated ID is: " + noamUser.getId());
    }

    /*
     * show how to get a collection of type List containing all of the records in the app_user table
     */
    private void showAllUsers() {
        Session session = theHibernateUtility.getCurrentSession();
        Transaction transaction = session.beginTransaction();
        /*
         * execute a HQL query against the database.  HQL is NOT SQL.  It is object based.
         */
        Query allUsersQuery = session.createQuery("select u from User as u order by u.id");
        /*
         * get a list of User instances based on what was found in the database tables.
         */
        users = allUsersQuery.list();
        // num users: 3
        System.out.println("num users: " + users.size());
        /*
         * iterate over each User instance returned by the query and found in the list.
         */
        // It goes through and gets how many telephones each user has
        Iterator<User> iter = users.iterator();
        while(iter.hasNext()) {
            User element = iter.next();
            System.out.println(element.toString());
            System.out.println("num of phone numbers: "+ element.getPhoneNumbers().size());
        }
        transaction.commit();
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

    private void addSharedPhoneNumber() {
        Session session = theHibernateUtility.getCurrentSession();
        Transaction transaction = session.beginTransaction();
       /*
        * get two User instances from the database using HQL.  This is NOT SQL.  It is object based.
        */
        Query sallyQuery = session.createQuery("select u from User as u where u.uname='sally'");
        User sallyUser = (User)sallyQuery.uniqueResult();

        Query dudeQuery = session.createQuery("select u from User as u where u.uname='dude'");
        User dudeUser = (User)dudeQuery.uniqueResult();

        // Bring all the user info and store it as an object base
        // This will through a null pointer exception because there is no Noam Chomsky anymore its Michele faulcault
        Query michelQuery = session.createQuery("select i from User as i where i.uname='Michel foucault'");
        User michelUser = (User)michelQuery.uniqueResult();

       /*
        * create a PhoneNumber instance
        */
        PhoneNumber sharedPhoneNumber = new PhoneNumber();
        sharedPhoneNumber.setPhone("(546)222-9898");
        // Lets add a phone number of Noam Chomsky
        PhoneNumber michelPhone = new PhoneNumber();
        michelPhone.setPhone("(777)777-777");

      /*
       * add the shared phone number to the noamUser
       */

        // Now bring a set of unique phones and add the one before this line
        Set<PhoneNumber> michelPhoneNum = michelUser.getPhoneNumbers();
        michelPhoneNum.add(michelPhone);


        Set<PhoneNumber> sallyPhoneNumbers = sallyUser.getPhoneNumbers();
        sallyPhoneNumbers.add(sharedPhoneNumber);

        // Share number with someone else
        Set<PhoneNumber> dudePhoneNumbers = dudeUser.getPhoneNumbers();
        dudePhoneNumbers.add(sharedPhoneNumber);

        // Now bring a set of unique phones and add the one before this line

      /*
       * inform the database that the phone number should be ready for permanent storage.
       */
        session.save(sharedPhoneNumber);
        // Here it goes Michel
        session.save(michelPhone);


        // Merge it with the rest
        session.merge(michelUser);
        session.merge(sallyUser);
        session.merge(dudeUser);
      /*
       * inform the database that the modified User instances should be ready for permanent storage.
       */
      /*
       * permanently store the changes into the database tables.
       */
        transaction.commit();
      /*
       * show that the database was updated by printing out all of the User instances created by a HQL query
       */
        showAllUsers();
    }
    private void deleteAddedUsers() {
//        Session session = theHibernateUtility.getCurrentSession();
//        Transaction transaction = session.beginTransaction();
//
//        int numUsers = users.size();
//        System.out.println("user count: " + numUsers);
//        for(int i = 0; i < numUsers; i++){
//            System.out.println("deleting user "+users.get(i).getUname());
//            session.delete(users.get(i));
//        }
//        transaction.commit();
//        /*
//          * at this point the records have been removed from the database but still exist in our class list attribute.
//          * Do not store lists retrieved from the database since they will be out of synch with the database table from which they come.
//          * This example shows that you should not store retrieved lists.
//          */
//        System.out.println(users);
//        users.clear();
//        /*
//          * now the Java instances are also gone and the database is back to its original state so the example application can be run again.
//          */
//        System.out.println(users);
    }
}
