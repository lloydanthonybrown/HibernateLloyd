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



}
