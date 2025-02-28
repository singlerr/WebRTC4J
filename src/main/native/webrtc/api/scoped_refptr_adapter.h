#include "api/scoped_refptr.h"
#ifndef _API_SCOPED_REFPTR_ADAPTER_H_
#define _API_SCOPED_REFPTR_ADAPTER_H_

template<class T> class ScopedRefPtrAdapter {
public:
    ScopedRefPtrAdapter(const T* ptr, int size, void *owner) :
        ptr((T*)ptr),
        size(size),
        owner(owner),
        smartPtr2(owner != NULL && owner != ptr ? *(rtc::scoped_refptr<T>*)owner : rtc::scoped_refptr<T>((T*)ptr)),
        smartPtr(smartPtr2) { }
    ScopedRefPtrAdapter(const rtc::scoped_refptr<T>& smartPtr) :
        ptr(0),
        size(0),
        owner(0),
        smartPtr2(smartPtr),
        smartPtr(smartPtr2) { }
    void assign(T* ptr, int size, void* owner) {
        this->ptr = ptr;
        this->size = size;
        this->owner = owner;
        this->smartPtr = owner != NULL && owner != ptr ? *(rtc::scoped_refptr<T>*)owner : rtc::scoped_refptr<T>((T*)ptr);
    }
    static void deallocate(void* owner) {
        delete (rtc::scoped_refptr<T>*)owner;
    }
    operator T*() {
        ptr = smartPtr.get();
        if (owner == NULL || owner == ptr) {
            owner = new rtc::scoped_refptr<T>(smartPtr);
        }
        return ptr;
    }
    operator rtc::scoped_refptr<T>&() {
        return smartPtr;
    }
    operator rtc::scoped_refptr<T>*() {
        return ptr ? &smartPtr : 0;
    }
    T* ptr;
    int size;
    void* owner;
    rtc::scoped_refptr<T> smartPtr2;
    rtc::scoped_refptr<T>& smartPtr;
};

#endif