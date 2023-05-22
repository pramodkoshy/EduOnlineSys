import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IQuestionItem } from 'app/shared/model/question-item.model';

type EntityResponseType = HttpResponse<IQuestionItem>;
type EntityArrayResponseType = HttpResponse<IQuestionItem[]>;

@Injectable({ providedIn: 'root' })
export class QuestionItemService {
  public resourceUrl = SERVER_API_URL + 'api/question-items';

  constructor(protected http: HttpClient) {}

  create(questionItem: IQuestionItem): Observable<EntityResponseType> {
    return this.http.post<IQuestionItem>(this.resourceUrl, questionItem, { observe: 'response' });
  }

  update(questionItem: IQuestionItem): Observable<EntityResponseType> {
    return this.http.put<IQuestionItem>(this.resourceUrl, questionItem, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IQuestionItem>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IQuestionItem[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
