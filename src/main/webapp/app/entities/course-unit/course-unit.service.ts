import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { ICourseUnit } from 'app/shared/model/course-unit.model';

type EntityResponseType = HttpResponse<ICourseUnit>;
type EntityArrayResponseType = HttpResponse<ICourseUnit[]>;

@Injectable({ providedIn: 'root' })
export class CourseUnitService {
  public resourceUrl = SERVER_API_URL + 'api/course-units';

  constructor(protected http: HttpClient) {}

  create(courseUnit: ICourseUnit): Observable<EntityResponseType> {
    return this.http.post<ICourseUnit>(this.resourceUrl, courseUnit, { observe: 'response' });
  }

  update(courseUnit: ICourseUnit): Observable<EntityResponseType> {
    return this.http.put<ICourseUnit>(this.resourceUrl, courseUnit, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ICourseUnit>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICourseUnit[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
