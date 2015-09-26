package ru.company.db;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.*;
import ru.company.beans.Pager;
import ru.company.entity.HibernateUtil;
import ru.company.entity.Author;
import ru.company.entity.Book;
import ru.company.entity.Genre;
import java.util.List;

public class DataSource {

    private SessionFactory sessionFactory;
    private static DataSource dataSource;
    private Pager currentPager;
    private DetachedCriteria currentCriteria;

    private DataSource() {
        sessionFactory = HibernateUtil.getSessionFactory();
    }

    public Session getSession() {
        return sessionFactory.getCurrentSession();
    }

    public static DataSource getInstance() {
        return dataSource == null ? dataSource = new DataSource() : dataSource;
    }

    public void runCurrentCriteria() {
        Criteria criteria = currentCriteria.addOrder(Order.asc("name")).getExecutableCriteria(getSession());
        List<Book> list = criteria.setFirstResult(currentPager.getFrom()).setMaxResults(currentPager.getTo()).list();
        currentPager.setList(list);
    }

    public List<Genre> getAllGeanrs() {
        return getSession().createCriteria(Genre.class).list();
    }

    public List<Author> getAllAuthors() {
        return getSession().createCriteria(Author.class).list();
    }

    public Author getAuthor(int id) {
        return (Author) getSession().get(Author.class, id);
    }

    public void getAllBooks(Pager pager) {
        currentPager = pager;
        int total = 0;

        Criteria criteria = getSession().createCriteria(Book.class);
        long buffer = (Long) criteria.setProjection(Projections.rowCount()).uniqueResult();
        total = (int) buffer;

        currentPager.setTotalBooksCount(total);
        currentCriteria = DetachedCriteria.forClass(Book.class);
        runCurrentCriteria();
    }

    public void getBooksByGenre(int genreId, Pager pager) {
        currentPager = pager;
        int total = 0;

        Criterion criterion = Restrictions.eq("genre.id", genreId);
        Criteria criteria = getSession().createCriteria(Book.class);
        long buffer = (Long) criteria.add(criterion).setProjection(Projections.rowCount()).uniqueResult();
        total = (int) buffer;
        currentPager.setTotalBooksCount(total);

        currentCriteria = DetachedCriteria.forClass(Book.class);
        currentCriteria.add(criterion);

        runCurrentCriteria();
    }

    public void getBooksByLetter(Character letter, Pager pager) {
        currentPager = pager;
        int total = 0;

        Criterion criterion = Restrictions.ilike("name", letter.toString(), MatchMode.START);

        Criteria criteria = getSession().createCriteria(Book.class);

        long buffer = (Long) criteria.add(criterion).setProjection(Projections.rowCount()).uniqueResult();
        total = (int) buffer;
        currentPager.setTotalBooksCount(total);

        currentCriteria = DetachedCriteria.forClass(Book.class);
        currentCriteria.add(criterion);

        runCurrentCriteria();
    }

    public void getBooksByAuthor(String authorName, Pager pager) {
        currentPager = pager;
        int total = 0;

        Criterion criterion = Restrictions.ilike("author.fio", authorName, MatchMode.ANYWHERE);

        Criteria criteria = getSession().createCriteria(Book.class, "book").createAlias("book.author", "author");
        long buffer = (Long) criteria.add(criterion).setProjection(Projections.rowCount()).uniqueResult();
        total = (int) buffer;
        currentPager.setTotalBooksCount(total);

        currentCriteria = DetachedCriteria.forClass(Book.class, "book").createAlias("book.author", "author");;
        currentCriteria.add(criterion);

        runCurrentCriteria();
    }

    public void getBooksByName(String bookName, Pager pager) {
        currentPager = pager;
        int total = 0;

        Criterion criterion = Restrictions.ilike("name", bookName, MatchMode.ANYWHERE);
        Criteria criteria = getSession().createCriteria(Book.class);
        long buffer = (Long) criteria.add(criterion).setProjection(Projections.rowCount()).uniqueResult();
        total = (int) buffer;
        currentPager.setTotalBooksCount(total);

        currentCriteria = DetachedCriteria.forClass(Book.class);
        currentCriteria.add(criterion);

        runCurrentCriteria();
    }

    public byte[] getContent(int id) {
        Criteria criteria = getSession().createCriteria(Book.class);
        criteria.setProjection(Property.forName("content"));
        criteria.add(Restrictions.eq("id", id));
        return (byte[]) criteria.uniqueResult();
    }

}
