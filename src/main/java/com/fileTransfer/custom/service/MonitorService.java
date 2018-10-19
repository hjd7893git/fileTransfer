package com.fileTransfer.custom.service;

import com.fileTransfer.custom.model.Overview;
import com.rest.frame.exception.UsualException;
import com.rest.frame.model.Table;
import com.rest.frame.service.InternalCache;
import org.ehcache.Cache;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by nano on 18-4-18.
 */
@Path("/monitor")
public class MonitorService {

    private InternalCache cache;
    private PersonalDAO dao;
    private Cache<String, Table> tables;

    public MonitorService() throws UsualException {
        super();
        this.cache = InternalCache.getInstance();
        this.dao = new PersonalDAO();
        this.tables = cache.getTables();
    }

    @GET
    @Path("/overview")
    @Produces(MediaType.APPLICATION_JSON)
    public Response overview() {
        HashMap<String, Long> res = dao.getOverview();
        List<LinkedHashMap<String, Object>> objs = dao.getOverview2();
        List<String> dateData = new ArrayList<>();
        List<Long> findData = new ArrayList<>();
        List<Long> finishedData = new ArrayList<>();

        objs.forEach(obj -> {
            obj.entrySet().iterator().forEachRemaining(i -> {
                if ("date".equals(i.getKey()))
                    dateData.add((String) i.getValue());
                if ("find".equals(i.getKey()))
                    findData.add(((BigDecimal) i.getValue()).longValue());
                if ("finish".equals(i.getKey()))
                    finishedData.add(((BigDecimal) i.getValue()).longValue());
            });
        });
        return Response.status(Response.Status.OK).entity(new Overview(res, dateData, findData, finishedData)).build();
    }
}
