import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { ICourseLearningObjects } from 'app/shared/model/course-learning-objects.model';

type EntityResponseType = HttpResponse<ICourseLearningObjects>;
type EntityArrayResponseType = HttpResponse<ICourseLearningObjects[]>;

@Injectable({ providedIn: 'root' })
export class CourseLearningObjectsService {
  public resourceUrl = SERVER_API_URL + 'api/course-learning-objects';

  constructor(protected http: HttpClient) {}

  create(courseLearningObjects: ICourseLearningObjects): Observable<EntityResponseType> {
    return this.http.post<ICourseLearningObjects>(this.resourceUrl, courseLearningObjects, { observe: 'response' });
  }

  update(courseLearningObjects: ICourseLearningObjects): Observable<EntityResponseType> {
    return this.http.put<ICourseLearningObjects>(this.resourceUrl, courseLearningObjects, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ICourseLearningObjects>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICourseLearningObjects[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
