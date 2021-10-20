import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IEdicao, getEdicaoIdentifier } from '../edicao.model';

export type EntityResponseType = HttpResponse<IEdicao>;
export type EntityArrayResponseType = HttpResponse<IEdicao[]>;

@Injectable({ providedIn: 'root' })
export class EdicaoService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/edicaos');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(edicao: IEdicao): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(edicao);
    return this.http
      .post<IEdicao>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(edicao: IEdicao): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(edicao);
    return this.http
      .put<IEdicao>(`${this.resourceUrl}/${getEdicaoIdentifier(edicao) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(edicao: IEdicao): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(edicao);
    return this.http
      .patch<IEdicao>(`${this.resourceUrl}/${getEdicaoIdentifier(edicao) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IEdicao>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IEdicao[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addEdicaoToCollectionIfMissing(edicaoCollection: IEdicao[], ...edicaosToCheck: (IEdicao | null | undefined)[]): IEdicao[] {
    const edicaos: IEdicao[] = edicaosToCheck.filter(isPresent);
    if (edicaos.length > 0) {
      const edicaoCollectionIdentifiers = edicaoCollection.map(edicaoItem => getEdicaoIdentifier(edicaoItem)!);
      const edicaosToAdd = edicaos.filter(edicaoItem => {
        const edicaoIdentifier = getEdicaoIdentifier(edicaoItem);
        if (edicaoIdentifier == null || edicaoCollectionIdentifiers.includes(edicaoIdentifier)) {
          return false;
        }
        edicaoCollectionIdentifiers.push(edicaoIdentifier);
        return true;
      });
      return [...edicaosToAdd, ...edicaoCollection];
    }
    return edicaoCollection;
  }

  protected convertDateFromClient(edicao: IEdicao): IEdicao {
    return Object.assign({}, edicao, {
      edicaoData: edicao.edicaoData?.isValid() ? edicao.edicaoData.format(DATE_FORMAT) : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.edicaoData = res.body.edicaoData ? dayjs(res.body.edicaoData) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((edicao: IEdicao) => {
        edicao.edicaoData = edicao.edicaoData ? dayjs(edicao.edicaoData) : undefined;
      });
    }
    return res;
  }
}
