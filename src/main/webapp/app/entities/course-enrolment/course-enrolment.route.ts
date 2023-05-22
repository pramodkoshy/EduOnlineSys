import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { ICourseEnrolment, CourseEnrolment } from 'app/shared/model/course-enrolment.model';
import { CourseEnrolmentService } from './course-enrolment.service';
import { CourseEnrolmentComponent } from './course-enrolment.component';
import { CourseEnrolmentDetailComponent } from './course-enrolment-detail.component';
import { CourseEnrolmentUpdateComponent } from './course-enrolment-update.component';

@Injectable({ providedIn: 'root' })
export class CourseEnrolmentResolve implements Resolve<ICourseEnrolment> {
  constructor(private service: CourseEnrolmentService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICourseEnrolment> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((courseEnrolment: HttpResponse<CourseEnrolment>) => {
          if (courseEnrolment.body) {
            return of(courseEnrolment.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new CourseEnrolment());
  }
}

export const courseEnrolmentRoute: Routes = [
  {
    path: '',
    component: CourseEnrolmentComponent,
    data: {
      authorities: [Authority.USER],
      defaultSort: 'id,asc',
      pageTitle: 'eduOnlineApp.courseEnrolment.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CourseEnrolmentDetailComponent,
    resolve: {
      courseEnrolment: CourseEnrolmentResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'eduOnlineApp.courseEnrolment.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CourseEnrolmentUpdateComponent,
    resolve: {
      courseEnrolment: CourseEnrolmentResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'eduOnlineApp.courseEnrolment.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CourseEnrolmentUpdateComponent,
    resolve: {
      courseEnrolment: CourseEnrolmentResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'eduOnlineApp.courseEnrolment.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];
