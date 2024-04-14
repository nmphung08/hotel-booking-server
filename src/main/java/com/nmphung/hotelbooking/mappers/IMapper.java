package com.nmphung.hotelbooking.mappers;

public interface IMapper<A, B> {
    public B mapTo(A a);

    public A mapFrom(B b);
}
