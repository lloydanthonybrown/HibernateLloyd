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
        aSillyHibernateUseExample.showAllUsers();


        }


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
              System.out.println("num users: "+users.size());
              /*
               * iterate over each User instance returned by the query and found in the list.
               */
              Iterator<User> iter = users.iterator();;
              while(iter.hasNext()) {
                  User element = iter.next();
                  System.out.println(element.toString());
                  System.out.println("num of phone numbers: " + element.getPhoneNumbers().size());
              }
              transaction.commit();
           }


}
