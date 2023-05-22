import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { ICourseModule } from 'app/shared/model/course-module.model';

type EntityResponseType = HttpResponse<ICourseModule>;
type EntityArrayResponseType = HttpResponse<ICourseModule[]>;

@Injectable({ providedIn: 'root' })
export class CourseModuleService {
  public resourceUrl = SERVER_API_URL + 'api/course-modules';

  constructor(protected http: HttpClient) {}

  create(courseModule: ICourseModule): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(courseModule);
    return this.http
      .post<ICourseModule>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(courseModule: ICourseModule): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(courseModule);
    return this.http
      .put<ICourseModule>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ICourseModule>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ICourseModule[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(courseModule: ICourseModule): ICourseModule {
    const copy: ICourseModule = Object.assign({}, courseModule, {
      startDate: courseModule.startDate && courseModule.startDate.isValid() ? courseModule.startDate.format(DATE_FORMAT) : undefined,
      endDate: courseModule.endDate && courseModule.endDate.isValid() ? courseModule.endDate.format(DATE_FORMAT) : undefined,
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.startDate = res.body.startDate ? moment(res.body.startDate) : undefined;
      res.body.endDate = res.body.endDate ? moment(res.body.endDate) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((courseModule: ICourseModule) => {
        courseModule.startDate = courseModule.startDate ? moment(courseModule.startDate) : undefined;
        courseModule.endDate = courseModule.endDate ? moment(courseModule.endDate) : undefined;
      });
    }
    return res;
  }
}
