import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { ICourseEnrolment } from 'app/shared/model/course-enrolment.model';

type EntityResponseType = HttpResponse<ICourseEnrolment>;
type EntityArrayResponseType = HttpResponse<ICourseEnrolment[]>;

@Injectable({ providedIn: 'root' })
export class CourseEnrolmentService {
  public resourceUrl = SERVER_API_URL + 'api/course-enrolments';

  constructor(protected http: HttpClient) {}

  create(courseEnrolment: ICourseEnrolment): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(courseEnrolment);
    return this.http
      .post<ICourseEnrolment>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(courseEnrolment: ICourseEnrolment): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(courseEnrolment);
    return this.http
      .put<ICourseEnrolment>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ICourseEnrolment>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ICourseEnrolment[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(courseEnrolment: ICourseEnrolment): ICourseEnrolment {
    const copy: ICourseEnrolment = Object.assign({}, courseEnrolment, {
      enrolmentDate:
        courseEnrolment.enrolmentDate && courseEnrolment.enrolmentDate.isValid()
          ? courseEnrolment.enrolmentDate.format(DATE_FORMAT)
          : undefined,
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.enrolmentDate = res.body.enrolmentDate ? moment(res.body.enrolmentDate) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((courseEnrolment: ICourseEnrolment) => {
        courseEnrolment.enrolmentDate = courseEnrolment.enrolmentDate ? moment(courseEnrolment.enrolmentDate) : undefined;
      });
    }
    return res;
  }
}
