package com.rivetlogic.birthday.service.result;

import com.liferay.portal.kernel.json.JSON;
import com.rivetlogic.birthday.model.UserBirthday;

import java.util.List;

@JSON(strict = true)
public class PaginatedResult<T> {

    public PaginatedResult(){}
    
    @JSON(strict = true)
    public static class Meta {

        private long offset;
        private long limit;

        private Meta() {}

        private Meta(long offset, long limit) {
            this.offset = offset;
            this.limit = limit;
        }

        @JSON
        public long getOffset() {
            return offset;
        }

        public void setOffset(long offset) {
            this.offset = offset;
        }

        @JSON
        public long getLimit() {
            return limit;
        }

        public void setLimit(long limit) {
            this.limit = limit;
        }
    }

    public PaginatedResult(Page<T> results, List<GroupedRecords<UserBirthday>>groupedRecords, long offset, long limit){
        this.users = results;
        this.dates = groupedRecords;
        this.metadata = new Meta(offset, limit);
    }
    
    public PaginatedResult(Page<T> results, List<GroupedRecords<UserBirthday>>groupedRecords){
        this.users = results;
        this.dates = groupedRecords;
        this.metadata = null;
    }
    
    public PaginatedResult(Page<T> results, long offset, long limit){
        this.users = results;
        this.metadata = new Meta(offset, limit);
    }
    
    private Meta metadata;
    private List<T> users;
    private List<GroupedRecords<UserBirthday>>dates;
    private String upcomingDate;
    
    @JSON
    public String getUpcomingDate() {
		return upcomingDate;
	}

	public void setUpcomingDate(String upcomingDate) {
		this.upcomingDate = upcomingDate;
	}

	@JSON
	public List<GroupedRecords<UserBirthday>> getDates() {
		return dates;
	}

	public void setDates(List<GroupedRecords<UserBirthday>> groupedResults) {
		this.dates = groupedResults;
	}

	@JSON
    public Meta getMetadata() {
		return metadata;
	}

	public void setMetadata(Meta metadata) {
		this.metadata = metadata;
	}
	
	@JSON
	public List<T> getUsers() {
        return users;
    }

    public void setUsers(List<T> results) {
        this.users = results;
    }
    
    public boolean hasResults(){
    	return users != null && !users.isEmpty();
    }
}
