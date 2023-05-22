import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IExamRegistration } from 'app/shared/model/exam-registration.model';

type EntityResponseType = HttpResponse<IExamRegistration>;
type EntityArrayResponseType = HttpResponse<IExamRegistration[]>;

@Injectable({ providedIn: 'root' })
export class ExamRegistrationService {
  public resourceUrl = SERVER_API_URL + 'api/exam-registrations';

  constructor(protected http: HttpClient) {}

  create(examRegistration: IExamRegistration): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(examRegistration);
    return this.http
      .post<IExamRegistration>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(examRegistration: IExamRegistration): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(examRegistration);
    return this.http
      .put<IExamRegistration>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IExamRegistration>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IExamRegistration[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(examRegistration: IExamRegistration): IExamRegistration {
    const copy: IExamRegistration = Object.assign({}, examRegistration, {
      enrolmentDate:
        examRegistration.enrolmentDate && examRegistration.enrolmentDate.isValid()
          ? examRegistration.enrolmentDate.format(DATE_FORMAT)
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
      res.body.forEach((examRegistration: IExamRegistration) => {
        examRegistration.enrolmentDate = examRegistration.enrolmentDate ? moment(examRegistration.enrolmentDate) : undefined;
      });
    }
    return res;
  }
}
