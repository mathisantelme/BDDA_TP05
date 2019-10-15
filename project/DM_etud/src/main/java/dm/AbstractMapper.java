package dm;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

/**
 * Implements common code to map an object to a row in a relational table
 */
public abstract class AbstractMapper {
    // object cache implemented by a map<key,object>
    protected Registry<Object, Object> loadedMap;
    // database object
    protected final DB db;

    /**
     * Init the cache and the database
     * @param dbName
     */
    public AbstractMapper(String dbName) {
        try {
            this.loadedMap = new Registry<>();
            this.db = DB.createDB(dbName);
        } catch (Exception e) {
            throw new RuntimeException("AbstractMapper:: Failed to initialize database access: " + e.getMessage());
        }
    }

    /**
     * Returns SQL insert string for domain object
     *
     * @return SQL insert string
     */
    protected abstract String insertStatement();

    /**
     * Returns SQL find string for domain object
     *
     * @return SQL find string
     */
    protected abstract String findStatement();

    /**
     * Returns SQL update string for domain object
     *
     * @return SQL update string
     */
    protected abstract String updateStatement();

    /**
     * Returns SQL delete string for domain object
     *
     * @return SQL delete string
     */
    protected abstract String deleteStatement();

    /**
     * Returns SQL delete string for all book objects
     * @return SQL delete string
     */
    protected abstract String deleteAllStatement();

    /**
     * Configure SQL insert PreparedStatement for domain object
     *
     * @param subject domain object
     * @param ps      SQL prepared statement
     * @throws MapperException if something goes wrong...
     */
    protected abstract void doInsert(DomainObject subject, PreparedStatement ps) throws MapperException;

    /**
     * Load a domain object from a SQL result set
     *
     * @param rs SQL result set
     * @return domain object
     * @throws MapperException if something goes wrong...
     */
    protected abstract DomainObject doLoad(ResultSet rs) throws MapperException;

    /**
     * Update a domain object from a SQL result set
     *
     * @param subject domain object
     * @param ps      SQL prepared statement
     * @throws MapperException if something goes wrong...
     */
    protected abstract void doUpdate(DomainObject subject, PreparedStatement ps) throws MapperException;

    /**
     * Insert an domain object in database
     *
     * @param object domain object
     * @return object identifier (oid)
     * @throws MapperException if something goes wrong...
     */
    protected Object abstractInsert(DomainObject object) throws MapperException {
        if (object == null) {
            throw new MapperException("AbstractMapper:: Insert failed because object is null...");
        } else {
            // get the object id
            Object subjectId = object.getId();
            if (subjectId == null) {
                throw new MapperException("AbstractMapper:: Insert failed because id is null...");
            } else {
                try {
                    // create a prepared sql statement
                    PreparedStatement insertStatement = db.prepare(insertStatement());
                    // set values into sql statement
                    doInsert(object, insertStatement);
                    // execute insert statement
                    insertStatement.execute();
                } catch (SQLException e) {
                    // transform exception type
                    throw new MapperException(e.getMessage());
                }
            }
            return subjectId;
        }
    }

    /**
     * Find an object with its oid
     * @param id object identifier
     * @return the object matching the iod
     * @throws MapperException if something goes wrong...
     */
    protected DomainObject abstractFind(Object id) throws MapperException {
        if (id == null) {
            throw new MapperException("AbstractMapper:: Find failed because id is null...");
        } else {
            try {
                // create a prepared SQL statement
                PreparedStatement findStatement = db.prepare(findStatement());

                // set id value into SQL statement
                findStatement.setObject(1, id);

                // execute select statement
                ResultSet rs = findStatement.executeQuery();

                // if there is one result
                if (rs.next()) {
                    // build and return the object
                    return load(rs);
                } else {
                    return null;
                }
            } catch (SQLException e) {
                // transform SQL exception into domain exception
                throw new MapperException(e.getMessage());
            }
        }
    }

    /**
     * Find the objects matching the selection criterion
     * @param criterion the criterion to retrieve many objects
     * @param findManyPattern SQL SELECT pattern
     * @return the domain objects corresponding to the result of the query
     * @throws MapperException if something goes wrong...
     */
    protected Set<DomainObject> abstractFindMany(Object criterion, String findManyPattern)
            throws MapperException {
        if (criterion == null && findManyPattern == null) {
            throw new MapperException("AbstractMapper:: Find failed because criterion and/or findManyPattern is/are null...");
        } else {
            try {
                // creating a prepared statement
                PreparedStatement findManyStatement = db.prepare(findManyPattern);

                // set criterion value into SQL statement
                findManyStatement.setObject(3, criterion);

                // executing the querry
                ResultSet rs = findManyStatement.executeQuery();

                // returning the values found
                return loadAll(rs);

            } catch (SQLException e) {
                throw new MapperException(e.getMessage());
            }
        }
    }

    /**
     * Update an object to the database
     *
     * @param updatedObject the object to update
     * @throws MapperException if something goes wrong...
     */
    protected void abstractUpdate(DomainObject updatedObject) throws MapperException {
        if (updatedObject == null) {
            throw new MapperException("AbstractMapper:: Find failed because specified object is null...");
        } else {
            try {
                // we catch the ID of the specified object
                Object id = updatedObject.getId();

                // if an object corresponding is present in cache, we delete it
                if (loadedMap.objectMap.containsKey(id))
                    loadedMap.objectMap.remove(id);

                // creation of a prepared update statement
                PreparedStatement updateStatement = db.prepare(updateStatement());

                // set id value into SQL statement
                doUpdate(updatedObject, updateStatement);

                // executing the statement
                int numRowAffected = updateStatement.executeUpdate();

                // if the number of row affected by the querry equals 0, then we throw an error
                if (numRowAffected == 0)
                    throw new MapperException("AbstractMapper:: Update failed because no corresponding objects were found");

            } catch (SQLException e) {
                throw new MapperException(e.getMessage());
            }
        }
    }

    /**
     * Delete an object from database
     * @param subject the object to delete
     * @throws MapperException if something goes wrong...
     */
    protected void abstractDelete(DomainObject subject) throws MapperException {
        if (subject == null) {
            throw new MapperException("AbstractMapper:: Delete failed because specified object is null...");
        } else {
            try {
                PreparedStatement deleteStatement = db.prepare(deleteStatement());

                // catching the ID of the specified object
                Object id = subject.getId();

                // if the object is already present in the cache we delete it
                if (loadedMap.objectMap.containsKey(id))
                    loadedMap.objectMap.remove(id);

                // we execute the request
                int numRowAffected = deleteStatement.executeUpdate();

                if (numRowAffected == 0)
                    throw new MapperException("AbstractMapper:: Delete failed because no corresponding objects were found");

            } catch (SQLException e) {
                throw new MapperException(e.getMessage());
            }
        }
    }

    /**
     * Delete all objects from database
     * @throws MapperException if something goes wrong...
     */
    protected void abstractDeleteAll() throws MapperException {
        try {
            // first we clear the content of the cache
            loadedMap.objectMap.clear();

            // then we create à deleteAll request
            PreparedStatement deleteAllStatement = db.prepare(deleteAllStatement());

            // we execute the satement
            deleteAllStatement.executeUpdate();

        } catch (SQLException e) {
            throw new MapperException(e.getMessage());
        }
    }

    /**
     * Load all the objects from a result set of SQL SELECT query.
     * For each object the load method (below) will be called.
     *
     * @param rs SQL result set
     * @return a set of domain objects
     * @throws MapperException if something goes wrong...
     */
    protected Set<DomainObject> loadAll(ResultSet rs) throws MapperException {
        try {
            Set<DomainObject> domainObjects = new HashSet<>();
            // for each SQL row
            while (rs.next()) {
                // create a domain object
                DomainObject domainObject = load(rs);
                domainObjects.add(domainObject);
            }
            return domainObjects;
        } catch (SQLException e) {
            throw new MapperException(e.getMessage());
        }
    }

    /**
     * Load a domain object from a result set of SQL query with
     * doLoad method implemented in a subclass of AbstractMapper.
     *
     * @param rs result set
     * @return a domain object
     * @throws MapperException if something goes wrong...
     */
    protected DomainObject load(ResultSet rs) throws MapperException {
        try {
            String id = new String(rs.getString(1)); // catching the id from the result

            // if the object is already loaded in the cache we return it
            if (loadedMap.objectMap.containsKey(id))
                return (DomainObject) loadedMap.objectMap.get(id);

            // if the objecct has been loaded yet, we load it up, add it to the cache and return it
            DomainObject result = doLoad(rs);
            loadedMap.objectMap.put(id, result);
            return result;
        } catch (SQLException e) {
            throw new MapperException(e.getMessage());
        }
    }

}

