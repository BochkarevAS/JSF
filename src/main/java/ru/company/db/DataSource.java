package ru.company.db;

import org.hibernate.*;
import org.hibernate.criterion.*;
import org.hibernate.transform.Transformers;
import ru.company.beans.Pager;
import ru.company.entity.*;

import java.util.List;

public class DataSource {

    private Pager pager = Pager.getInstance();
    private SessionFactory sessionFactory = null;
    private static DataSource dataSource;
    private DetachedCriteria bookListCriteria;
    private DetachedCriteria booksCountCriteria;
    private ProjectionList bookProjection;

    private DataSource() {

        prepareCriterias();

        sessionFactory = HibernateUtil.getSessionFactory();

        bookProjection = Projections.projectionList();
        bookProjection.add(Projections.property("id"), "id");
        bookProjection.add(Projections.property("name"), "name");
        bookProjection.add(Projections.property("image"), "image");
        bookProjection.add(Projections.property("genre"), "genre");
        bookProjection.add(Projections.property("pageCount"), "pageCount");
        bookProjection.add(Projections.property("isbn"), "isbn");
        bookProjection.add(Projections.property("publisher"), "publisher");
        bookProjection.add(Projections.property("author"), "author");
        bookProjection.add(Projections.property("publishYear"), "publishYear");
        bookProjection.add(Projections.property("descr"), "descr");
    }

    public static DataSource getInstance() {
        if (dataSource == null) {
            dataSource = new DataSource();
        }
        return dataSource;
    }

    private Session getSession() {
        return sessionFactory.getCurrentSession();
    }

    public List<Genre> getAllGenres() {
        return getSession().createCriteria(Genre.class).list();
    }

    public List<Publisher> getAllPublishers() {
        return getSession().createCriteria(Publisher.class).list();
    }

    public List<Author> getAllAuthors() {
        return getSession().createCriteria(Author.class).list();
    }

    public Author getAuthor(int id) {
        return (Author) getSession().get(Author.class, id);
    }

    public void getAllBooks() {
        prepareCriterias();
        populateList();
    }

    public void getBooksByGenre(int genreId) {

        Criterion criterion = Restrictions.eq("genre.id", genreId);

        prepareCriterias(criterion);
        populateList();
    }

    public void getBooksByLetter(Character letter) {

        Criterion criterion = Restrictions.ilike("b.name", letter.toString(), MatchMode.START);

        prepareCriterias(criterion);
        populateList();
    }

    public void getBooksByAuthor(String authorName) {

        Criterion criterion = Restrictions.ilike("author.fio", authorName, MatchMode.ANYWHERE);

        prepareCriterias(criterion);
        populateList();
    }

    public void getBooksByName(String bookName) {

        Criterion criterion = Restrictions.ilike("b.name", bookName, MatchMode.ANYWHERE);

        prepareCriterias(criterion);
        populateList();
    }

    public byte[] getContent(int id) {
        Criteria criteria = getSession().createCriteria(Book.class);
        criteria.setProjection(Property.forName("content"));
        criteria.add(Restrictions.eq("id", id));
        return (byte[]) criteria.uniqueResult();
    }

    private void runBookListCriteria() {
        Criteria criteria = bookListCriteria.getExecutableCriteria(getSession());
        criteria.addOrder(Order.asc("b.name")).setProjection(bookProjection).setResultTransformer(Transformers.aliasToBean(Book.class));

        criteria.setFirstResult(pager.getFrom()).setMaxResults(pager.getTo());

        List<Book> list = criteria.list();
        pager.setList(list);
    }

    private void runCountCriteria() {
        Criteria criteria = booksCountCriteria.getExecutableCriteria(getSession());
        long totalLong = (Long) criteria.setProjection(Projections.rowCount()).uniqueResult();
        int total = (int) totalLong;

        pager.setTotalBooksCount(total);
    }

    public void update() {
//        Session session = sessionFactory.openSession();
//        Transaction transaction = session.getTransaction();
//        transaction.begin();
        for (Object object : pager.getList()) {

            Book book = (Book) object;
            getSession().update(book);


        }
//        transaction.commit();
//        session.flush();
//        session.close();

    }

    public void addBook(Book book) {
        getSession().save(book);
    }

    private void prepareCriterias(Criterion criterion) {
        bookListCriteria = DetachedCriteria.forClass(Book.class, "b");
        createAliases(bookListCriteria);
        bookListCriteria.add(criterion);

        booksCountCriteria = DetachedCriteria.forClass(Book.class, "b");
        createAliases(booksCountCriteria);
        booksCountCriteria.add(criterion);
    }

    private void prepareCriterias() {
        bookListCriteria = DetachedCriteria.forClass(Book.class, "b");
        createAliases(bookListCriteria);

        booksCountCriteria = DetachedCriteria.forClass(Book.class, "b");
        createAliases(booksCountCriteria);
    }

    private void createAliases(DetachedCriteria criteria) {
        criteria.createAlias("b.author", "author");
        criteria.createAlias("b.genre", "genre");
        criteria.createAlias("b.publisher", "publisher");
    }

    public void populateList() {
        runCountCriteria();
        runBookListCriteria();
    }

    public void updateBook(Book book) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("update Book ");
        queryBuilder.append("set name = :name, ");
        queryBuilder.append("pageCount = :pageCount, ");
        queryBuilder.append("isbn = :isbn, ");
        queryBuilder.append("genre = :genre, ");
        queryBuilder.append("author = :author, ");
        queryBuilder.append("publishYear = :publishYear, ");
        queryBuilder.append("publisher = :publisher, ");



        queryBuilder.append("descr = :descr ");

        queryBuilder.append("where id = :id");


        Query query = getSession().createQuery(queryBuilder.toString());


        query.setParameter("name", book.getName());
        query.setParameter("pageCount", book.getPageCount());
        query.setParameter("isbn", book.getIsbn());
        query.setParameter("genre", book.getGenre());
        query.setParameter("author", book.getAuthor());
        query.setParameter("publishYear", book.getPublishYear());
        query.setParameter("publisher", book.getPublisher());
        query.setParameter("descr", book.getDescr());
        query.setParameter("id", book.getId());


        int result = query.executeUpdate();
    }

    public void deleteBook(Book book) {
        Query query = getSession().createQuery("delete from Book where id = :id");
        query.setParameter("id", book.getId());
        int result = query.executeUpdate();
    }

}