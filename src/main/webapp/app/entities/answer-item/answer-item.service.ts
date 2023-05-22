import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IAnswerItem } from 'app/shared/model/answer-item.model';

type EntityResponseType = HttpResponse<IAnswerItem>;
type EntityArrayResponseType = HttpResponse<IAnswerItem[]>;

@Injectable({ providedIn: 'root' })
export class AnswerItemService {
  public resourceUrl = SERVER_API_URL + 'api/answer-items';

  constructor(protected http: HttpClient) {}

  create(answerItem: IAnswerItem): Observable<EntityResponseType> {
    return this.http.post<IAnswerItem>(this.resourceUrl, answerItem, { observe: 'response' });
  }

  update(answerItem: IAnswerItem): Observable<EntityResponseType> {
    return this.http.put<IAnswerItem>(this.resourceUrl, answerItem, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IAnswerItem>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IAnswerItem[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
