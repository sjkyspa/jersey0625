package org.thoughtworks.com.resource;

import org.thoughtworks.com.domain.Product;
import org.thoughtworks.com.json.request.CreateProductJson;
import org.thoughtworks.com.json.response.ProductJson;
import org.thoughtworks.com.json.response.ProductRefJson;
import org.thoughtworks.com.provider.ProductRepository;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Path("/products")
public class ProductResource {

    @Inject
    ProductRepository productRepository;

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public ProductJson getProduct(@PathParam("id") int id, @Context UriInfo uriInfo) {
        Product product = productRepository.getProductById(id);
        return new ProductJson(product, uriInfo);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<ProductRefJson> getProductList(@Context UriInfo uriInfo) {
        List<Product> productList = productRepository.getProductList();
        return productList.stream().map(p -> new ProductRefJson(p, uriInfo)).collect(toList());
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createProduct(@Context UriInfo uriInfo, CreateProductJson createProductJson) {
        int productId = productRepository.createProduct(createProductJson.createProduct());
        return Response.created(uriInfo.getAbsolutePathBuilder().path(String.valueOf(productId)).build()).build();
    }


    @Path("{id}/prices")
    public PriceResource getPrice(@PathParam("id") int id) {
        Product product = productRepository.getProductById(id);
        return new PriceResource(productRepository, product);
    }
}
