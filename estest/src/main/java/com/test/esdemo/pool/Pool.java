package com.test.esdemo.pool;


import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.elasticsearch.ElasticsearchException;

public class Pool<T> implements Cloneable {
    protected GenericObjectPool<T> internalPool;
    public Pool(){
        super();
    }
    public Pool(final GenericObjectPoolConfig poolConfig, PooledObjectFactory<T> pooledObjectFactory){
        initPool(poolConfig,pooledObjectFactory);
    }

    public void initPool(final GenericObjectPoolConfig poolConfig, PooledObjectFactory<T> pooledObjectFactory) {
        if(this.internalPool!=null){
            closeInternalPool();
        }
        this.internalPool = new GenericObjectPool<T>(pooledObjectFactory,poolConfig);
    }

    private void closeInternalPool() {
        try{
            internalPool.close();
        }catch(Exception e){
            throw new ElasticsearchException("could not destroy the pool",e);
        }
    }

    public T getResource(){
        try{
            return internalPool.borrowObject();
        }catch(Exception e){
            throw new ElasticsearchException("could not get resource from pool",e);
        }
    }

    public void  returnResource(final T resource){
        if(resource != null){
            returnResourceObject(resource);
        }
    }

    private void returnResourceObject(T resource) {
        if(resource == null){
            return;
        }
        try{
            internalPool.returnObject(resource);
        }catch(Exception e){
            throw new ElasticsearchException("could not return the resource to the pool",e);
        }
    }

    public void returnBrokenResource(final T resource){
        if(resource != null){
            returnBrokenResourceObject(resource);
        }
    }

    private void returnBrokenResourceObject(T resource){
        try{
            internalPool.invalidateObject(resource);
        }catch(Exception e){
            throw new ElasticsearchException("could not return the resource to the pool",e);
        }
    }

    public void destroy(){
        closeInternalPool();
    }

    public int getNumActive(){
        if(poolInactive()){
            return -1;
        }
        return this.internalPool.getNumActive();
    }

    public int getNumIdle(){
        if(poolInactive()){
            return -1;
        }
        return this.internalPool.getNumIdle();
    }

    public long getMeanBorrowWaitTimeMillis(){
        if (poolInactive()) {
            return -1;
        }

        return this.internalPool.getMeanBorrowWaitTimeMillis();
    }


    private boolean poolInactive() {
        return this.internalPool == null || this.internalPool.isClosed();
    }

    public void addObject(int count)throws Exception{
        try {
            for (int i = 0; i < count; i++) {
                this.internalPool.addObject();
            }
        } catch (Exception e) {
            throw new Exception("Error trying to add idle objects", e);
        }

    }


}
